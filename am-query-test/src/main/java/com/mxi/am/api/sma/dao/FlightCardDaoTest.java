package com.mxi.am.api.sma.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.api.sma.dao.impl.JdbcFlightCardDao;
import com.mxi.am.api.sma.dao.model.AircraftDetails;
import com.mxi.am.api.sma.dao.model.FlightCard;
import com.mxi.am.api.sma.dao.model.WorkPackageDetails;
import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.am.util.AuthorityTestUtility;
import com.mxi.mx.common.utils.DateUtils;
import com.mxi.mx.core.key.AuthorityKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.TaskKey;


public class FlightCardDaoTest {

   @Rule
   public DatabaseConnectionRule databaseConnectionRule = new DatabaseConnectionRule();

   private AuthorityTestUtility authorityUtil = new AuthorityTestUtility();

   // Users
   private static final HumanResourceKey YOW_LINE_SUPERVISOR = new HumanResourceKey( "4650:10" );
   private static final HumanResourceKey YYZ_LINE_SUPERVISOR = new HumanResourceKey( "4650:20" );
   private static final HumanResourceKey YYG_LINE_SUPERVISOR = new HumanResourceKey( "4650:710" );
   private static final HumanResourceKey LAX_LINE_SUPERVISOR = new HumanResourceKey( "4650:610" );

   // AC-AIR Aircraft Data
   private static final InventoryKey AIRCRAFT_IN_AIR = new InventoryKey( "4650:1000" );
   private static final TaskKey AIRCRAFT_IN_AIR_WORK_PACKAGE = new TaskKey( "4650:1234" );

   private static final Integer IN_AIR_REMAINING_TASK_COUNT = 3;
   private static final Integer IN_AIR_TOTAL_TASK_COUNT = 3;
   private static final int IN_AIR_PACKAGED_FAULT_COUNT = 1;
   private static final int IN_AIR_UNPACKAGED_FAULT_COUNT = 2;

   // AC-GRND Aircraft Data
   private static final InventoryKey AIRCRAFT_ON_GROUND = new InventoryKey( "4650:2000" );
   private static final TaskKey AIRCRAFT_ON_GROUND_WORK_PACKAGE = new TaskKey( "4650:5678" );
   private static final Integer ON_GROUND_REMAINING_TASK_COUNT = 3;
   private static final Integer ON_GROUND_TOTAL_TASK_COUNT = 4;
   private static final int ON_GROUND_PACKAGED_FAULT_COUNT = 2;
   private static final int ON_GROUND_UNPACKAGED_FAULT_COUNT = 1;

   // AC-GONE used to show completed flight legs are ignored
   private static final InventoryKey AC_GONE = new InventoryKey( "4650:3000" );

   // Time bound tests Aircraft Information
   private static final InventoryKey AIRCRAFT_IN = new InventoryKey( "4650:71000" );
   private static final InventoryKey AIRCRAFT_OUT = new InventoryKey( "4650:81000" );
   private static final InventoryKey AIRCRAFT_INOUT_OUT_OUT_OF_BOUNDS =
         new InventoryKey( "4650:91000" );

   // Aircrafts for Rejecting Completed Legs
   private static final InventoryKey AC_IN_LAX = new InventoryKey( "4650:77777" );

   private FlightCardDao flightCardDao;


   @Before
   public void setUp() {
      DataLoaders.load( databaseConnectionRule.getConnection(), getClass() );
      flightCardDao = new JdbcFlightCardDao();
   }


   @Test
   public void includesCardsForAircraftInventoryWithNoAuthoritySet() {
      authorityUtil.setAllAuthority( YOW_LINE_SUPERVISOR, false );
      authorityUtil.setAuthority( AIRCRAFT_IN_AIR, Optional.empty() );

      List<FlightCard> flightCards = flightCardDao.find( YOW_LINE_SUPERVISOR, true, 24 );
      assertEquals( 1, flightCards.size() );

      getCard( AIRCRAFT_IN_AIR, flightCards );
   }


   @Test
   public void includesCardsForAircraftInventoryThatUserHasAuthorityOver() {
      authorityUtil.setAllAuthority( YOW_LINE_SUPERVISOR, false );
      authorityUtil.setAuthority( AIRCRAFT_IN_AIR, Optional.of( new AuthorityKey( "4650:1235" ) ) );
      authorityUtil.setAuthority( AIRCRAFT_ON_GROUND,
            Optional.of( new AuthorityKey( "4650:1234" ) ) );
      authorityUtil.giveAuthority( YOW_LINE_SUPERVISOR, new AuthorityKey( "4650:1234" ) );

      List<FlightCard> flightCards = flightCardDao.find( YOW_LINE_SUPERVISOR, true, 24 );
      assertEquals( 1, flightCards.size() );

      getCard( AIRCRAFT_ON_GROUND, flightCards );
   }


   @Test
   public void includesCardsForAnyAircraftAuthorityWhenUserHasAllAuthority() {
      authorityUtil.setAllAuthority( YOW_LINE_SUPERVISOR, true );
      authorityUtil.setAuthority( AIRCRAFT_IN_AIR, Optional.of( new AuthorityKey( "4650:1235" ) ) );
      authorityUtil.setAuthority( AIRCRAFT_ON_GROUND,
            Optional.of( new AuthorityKey( "4650:1234" ) ) );

      List<FlightCard> flightCards = flightCardDao.find( YOW_LINE_SUPERVISOR, true, 24 );
      assertEquals( 2, flightCards.size() );

      getCard( AIRCRAFT_IN_AIR, flightCards );
      getCard( AIRCRAFT_ON_GROUND, flightCards );
   }


   @Test
   public void includesAircraftDetailsOnEachFlightCard() {
      List<FlightCard> flightCards = flightCardDao.find( YOW_LINE_SUPERVISOR, true, 24 );
      assertEquals( 2, flightCards.size() );

      FlightCard airCard = getCard( AIRCRAFT_IN_AIR, flightCards );
      FlightCard groundCard = getCard( AIRCRAFT_ON_GROUND, flightCards );

      AircraftDetails inAirAircraftDetails = AircraftDetails.builder().assembly( "ACFT1" )
            .key( AIRCRAFT_IN_AIR.toValueString() ).location( "OPS" ).operationalStatus( "NORM" )
            .registrationCode( "AC-AIR" ).build();
      AircraftDetails onGroundAircraftDetails = AircraftDetails.builder().assembly( "ACFT1" )
            .key( AIRCRAFT_ON_GROUND.toValueString() ).location( "YOW/LINE1" )
            .operationalStatus( "NORM" ).registrationCode( "AC-GRND" ).build();

      assertEquals( inAirAircraftDetails, airCard.getAircraft() );
      assertEquals( onGroundAircraftDetails, groundCard.getAircraft() );
   }


   @Test
   public void displaysFlightDetailsBasedOnTheCurrentUserStationAssignment() {
      List<FlightCard> flightCards = flightCardDao.find( YOW_LINE_SUPERVISOR, true, 24 );
      assertEquals( 2, flightCards.size() );

      FlightCard airCard = getCard( AIRCRAFT_IN_AIR, flightCards );
      FlightCard groundCard = getCard( AIRCRAFT_ON_GROUND, flightCards );

      // Flight data visible to YOW Supervisor
      assertTrue( airCard.getArrivalLeg().isPresent() );
      assertEquals( "1234", airCard.getArrivalLeg().get().getFlightNo() );
      assertEquals( "MXOFF", airCard.getArrivalLeg().get().getFlightStatus() );
      assertEquals( "2", airCard.getArrivalLeg().get().getGate() );
      assertDateIsOffsetBy( airCard.getArrivalLeg().get().getActualDate(), 1 );
      assertDateIsOffsetBy( airCard.getArrivalLeg().get().getScheduledDate(), 1 );
      assertTrue( airCard.getDepartureLeg().isPresent() );
      assertEquals( "1235", airCard.getDepartureLeg().get().getFlightNo() );
      assertEquals( "MXPLAN", airCard.getDepartureLeg().get().getFlightStatus() );
      assertEquals( "2", airCard.getDepartureLeg().get().getGate() );
      assertDateIsOffsetBy( airCard.getDepartureLeg().get().getActualDate(), 6 );
      assertDateIsOffsetBy( airCard.getDepartureLeg().get().getScheduledDate(), 6 );

      assertFalse( groundCard.getArrivalLeg().isPresent() );
      assertTrue( groundCard.getDepartureLeg().isPresent() );
      assertEquals( "5678", groundCard.getDepartureLeg().get().getFlightNo() );
      assertEquals( "MXPLAN", groundCard.getDepartureLeg().get().getFlightStatus() );
      assertEquals( "2", groundCard.getDepartureLeg().get().getGate() );
      assertDateIsOffsetBy( groundCard.getDepartureLeg().get().getActualDate(), 2 );
      assertDateIsOffsetBy( groundCard.getDepartureLeg().get().getScheduledDate(), 2 );

      flightCards = flightCardDao.find( YYZ_LINE_SUPERVISOR, true, 24 );
      assertEquals( 2, flightCards.size() );

      airCard = getCard( AIRCRAFT_IN_AIR, flightCards );
      groundCard = getCard( AIRCRAFT_ON_GROUND, flightCards );

      // Flight data visible to YYZ Supervisor
      assertTrue( airCard.getArrivalLeg().isPresent() );
      assertEquals( "1235", airCard.getArrivalLeg().get().getFlightNo() );
      assertEquals( "MXPLAN", airCard.getArrivalLeg().get().getFlightStatus() );
      assertEquals( "3", airCard.getArrivalLeg().get().getGate() );
      assertDateIsOffsetBy( airCard.getArrivalLeg().get().getActualDate(), 12 );
      assertDateIsOffsetBy( airCard.getArrivalLeg().get().getScheduledDate(), 12 );
      assertFalse( airCard.getDepartureLeg().isPresent() );

      assertTrue( groundCard.getArrivalLeg().isPresent() );
      assertEquals( "5678", groundCard.getArrivalLeg().get().getFlightNo() );
      assertEquals( "MXPLAN", groundCard.getArrivalLeg().get().getFlightStatus() );
      assertEquals( "1", groundCard.getArrivalLeg().get().getGate() );
      assertDateIsOffsetBy( groundCard.getArrivalLeg().get().getActualDate(), 4 );
      assertDateIsOffsetBy( groundCard.getArrivalLeg().get().getScheduledDate(), 4 );
      assertTrue( groundCard.getDepartureLeg().isPresent() );
      assertEquals( "5678", groundCard.getDepartureLeg().get().getFlightNo() );
      assertEquals( "MXPLAN", groundCard.getDepartureLeg().get().getFlightStatus() );
      assertEquals( "2", groundCard.getDepartureLeg().get().getGate() );
      assertDateIsOffsetBy( groundCard.getDepartureLeg().get().getActualDate(), 2 );
      assertDateIsOffsetBy( groundCard.getDepartureLeg().get().getScheduledDate(), 2 );
   }


   @Test
   public void displaysLocationDetailsBasedOnTheCurrentUserStationAssignment() {
      // YOW user should see:
      // AC-AIR: YYZ YOW YYZ
      // AC-GRND: -- YOW YYZ
      List<FlightCard> flightCards = flightCardDao.find( YOW_LINE_SUPERVISOR, true, 24 );
      assertEquals( 2, flightCards.size() );

      FlightCard airCard = getCard( AIRCRAFT_IN_AIR, flightCards );
      FlightCard groundCard = getCard( AIRCRAFT_ON_GROUND, flightCards );

      assertEquals( "YYZ", airCard.getPreviousLocation() );
      assertEquals( "YOW", airCard.getCenterLocation() );
      assertEquals( "YYZ", airCard.getNextLocation() );

      assertNull( groundCard.getPreviousLocation() );
      assertEquals( "YOW", groundCard.getCenterLocation() );
      assertEquals( "YYZ", groundCard.getNextLocation() );

      // YYZ user should see:
      // AC-AIR: -- YYZ YOW
      // AC-GRND: YOW YYZ YOW
      flightCards = flightCardDao.find( YYZ_LINE_SUPERVISOR, true, 24 );
      assertEquals( 2, flightCards.size() );

      airCard = getCard( AIRCRAFT_IN_AIR, flightCards );
      groundCard = getCard( AIRCRAFT_ON_GROUND, flightCards );

      assertEquals( "YOW", airCard.getPreviousLocation() );
      assertEquals( "YYZ", airCard.getCenterLocation() );
      assertNull( airCard.getNextLocation() );

      assertEquals( "YOW", groundCard.getPreviousLocation() );
      assertEquals( "YYZ", groundCard.getCenterLocation() );
      assertEquals( "YOW", groundCard.getNextLocation() );
   }


   @Test
   public void excludesWPSummaryWhenUserIsNotAssignedToWPLocation() {
      List<FlightCard> flightCards = flightCardDao.find( YYZ_LINE_SUPERVISOR, true, 24 );
      assertEquals( 2, flightCards.size() );

      FlightCard airCard = getCard( AIRCRAFT_IN_AIR, flightCards );
      FlightCard groundCard = getCard( AIRCRAFT_ON_GROUND, flightCards );

      // YYZ Line Supervisor should not have work package data because they are scheduled at YOW
      assertFalse( airCard.getWorkPackage().isPresent() );
      assertFalse( groundCard.getWorkPackage().isPresent() );
   }


   @Test
   public void includesWPSummaryWhenUserIsAssignedToWPLocation() {
      List<FlightCard> flightCards = flightCardDao.find( YOW_LINE_SUPERVISOR, true, 24 );
      assertEquals( 2, flightCards.size() );

      FlightCard airCard = getCard( AIRCRAFT_IN_AIR, flightCards );
      FlightCard groundCard = getCard( AIRCRAFT_ON_GROUND, flightCards );

      // YOW Line Supervisor should see work package data because they are scheduled at YOW
      Optional<WorkPackageDetails> inAirWorkPackageDetails = airCard.getWorkPackage();
      assertTrue( inAirWorkPackageDetails.isPresent() );
      assertEquals( AIRCRAFT_IN_AIR_WORK_PACKAGE.toValueString(),
            inAirWorkPackageDetails.get().getKey() );
      assertEquals( "AC-AIR WP", inAirWorkPackageDetails.get().getName() );
      assertEquals( IN_AIR_REMAINING_TASK_COUNT.intValue(),
            inAirWorkPackageDetails.get().getRemainingTaskCount() );
      assertDateIsOffsetBy( inAirWorkPackageDetails.get().getStartDate(), 2 );
      assertEquals( "COMMIT", inAirWorkPackageDetails.get().getStatus() );
      assertEquals( IN_AIR_TOTAL_TASK_COUNT.intValue(),
            inAirWorkPackageDetails.get().getTotalTaskCount() );

      Optional<WorkPackageDetails> onGroundWorkPackageDetails = groundCard.getWorkPackage();
      assertTrue( onGroundWorkPackageDetails.isPresent() );
      assertEquals( AIRCRAFT_ON_GROUND_WORK_PACKAGE.toValueString(),
            onGroundWorkPackageDetails.get().getKey() );
      assertEquals( "AC-GRND WP", onGroundWorkPackageDetails.get().getName() );
      assertEquals( ON_GROUND_REMAINING_TASK_COUNT.intValue(),
            onGroundWorkPackageDetails.get().getRemainingTaskCount() );
      assertDateIsOffsetBy( onGroundWorkPackageDetails.get().getStartDate(), -1 );
      assertEquals( "IN WORK", onGroundWorkPackageDetails.get().getStatus() );
      assertEquals( ON_GROUND_TOTAL_TASK_COUNT.intValue(),
            onGroundWorkPackageDetails.get().getTotalTaskCount() );
   }


   @Test
   public void includesOpenFaultCountsForUserAssignedToGroundLocation() {
      List<FlightCard> flightCards = flightCardDao.find( YOW_LINE_SUPERVISOR, true, 24 );
      assertEquals( 2, flightCards.size() );

      FlightCard airCard = getCard( AIRCRAFT_IN_AIR, flightCards );
      FlightCard groundCard = getCard( AIRCRAFT_ON_GROUND, flightCards );

      assertEquals( IN_AIR_PACKAGED_FAULT_COUNT, airCard.getOpenPackagedFaultsCount() );
      assertEquals( IN_AIR_UNPACKAGED_FAULT_COUNT, airCard.getOpenUnpackagedFaultsCount() );
      assertEquals( ON_GROUND_PACKAGED_FAULT_COUNT, groundCard.getOpenPackagedFaultsCount() );
      assertEquals( ON_GROUND_UNPACKAGED_FAULT_COUNT, groundCard.getOpenUnpackagedFaultsCount() );
   }


   @Test
   public void excludesOpenFaultCountsForUserNotAssignedToGroundLocation() {
      List<FlightCard> flightCards = flightCardDao.find( YYZ_LINE_SUPERVISOR, true, 24 );
      assertEquals( 2, flightCards.size() );

      FlightCard airCard = getCard( AIRCRAFT_IN_AIR, flightCards );
      FlightCard groundCard = getCard( AIRCRAFT_ON_GROUND, flightCards );

      assertEquals( 0, airCard.getOpenPackagedFaultsCount() );
      assertEquals( 0, airCard.getOpenUnpackagedFaultsCount() );
      assertEquals( 0, groundCard.getOpenPackagedFaultsCount() );
      assertEquals( 0, groundCard.getOpenUnpackagedFaultsCount() );
   }


   @Test
   public void excludesWPSummaryWhenWPIsScheduledOutsideOfTimeWindow() {
      List<FlightCard> flightCards = flightCardDao.find( YOW_LINE_SUPERVISOR, true, 1 );
      assertEquals( 1, flightCards.size() );

      FlightCard airCard = getCard( AIRCRAFT_IN_AIR, flightCards );

      // Work package is schedule in 2 hours from now, but the time window is only 1 hour
      assertFalse( airCard.getWorkPackage().isPresent() );
   }


   @Test
   public void timeFilter_onlyArrivingLeg() {
      List<FlightCard> flightCards = flightCardDao.find( YYG_LINE_SUPERVISOR, true, 8 );

      assertEquals( 1, flightCards.size() );
      getCard( AIRCRAFT_IN, flightCards );
   }


   @Test
   public void timeFilter_onlyDepartingLeg() {
      List<FlightCard> flightCards = flightCardDao.find( YYG_LINE_SUPERVISOR, true, 16 );

      assertEquals( 2, flightCards.size() );
      getCard( AIRCRAFT_OUT, flightCards );
   }


   @Test
   public void timeFilter_hasBothLegs() {
      List<FlightCard> flightCards = flightCardDao.find( YYG_LINE_SUPERVISOR, true, 24 );

      assertEquals( 3, flightCards.size() );
      getCard( AIRCRAFT_INOUT_OUT_OUT_OF_BOUNDS, flightCards );
   }


   @Test
   public void assertCompletedArrivalIsIgnored() {
      List<FlightCard> flightCards = flightCardDao.find( LAX_LINE_SUPERVISOR, true, 24 );

      FlightCard flightCardNulledArrival = getCard( AC_IN_LAX, flightCards );
      assertEquals( Optional.empty(), flightCardNulledArrival.getArrivalLeg() );
   }


   /*
    * Asserts that a date is the specified offset from the current time (within 1 minute of
    * accuracy)
    */
   private void assertDateIsOffsetBy( Date date, int offsetHours ) {
      Date adjustedDate = DateUtils.addHours( new Date(), offsetHours );
      assertTrue(
            String.format( "Provided date: %s was not a %d hour offset from the current date.",
                  date, offsetHours ),
            Math.abs( date.getTime() - adjustedDate.getTime() ) < 60000 );
   }


   /*
    * Retrieves a flight card for the specified aircraft key from a list of flight card while
    * asserting existence.
    */
   private FlightCard getCard( InventoryKey aircraftKey, List<FlightCard> flightCards ) {
      assertFalse( String.format(
            "There were no flight cards in the list when trying to retrieve the flight card for aircraft: %s",
            aircraftKey.toValueString() ), flightCards.isEmpty() );

      Optional<FlightCard> flightCard = flightCards.stream()
            .filter( card -> aircraftKey.toValueString().equals( card.getAircraft().getKey() ) )
            .findFirst();

      assertTrue(
            String.format( "Expected a flight card to exist for aircraft: %s, but it did not.",
                  aircraftKey.toValueString() ),
            flightCard.isPresent() );

      return flightCard.get();
   }

}
