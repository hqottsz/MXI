package com.mxi.am.stepdefn.persona.techrecords;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.junit.Assert;

import com.mxi.am.driver.query.InventoryQueriesDriver;
import com.mxi.am.driver.web.NavigationDriver;
import com.mxi.am.driver.web.flight.EditFlightPageDriver;
import com.mxi.am.driver.web.flight.flightDetails.FlightDetailsPageDriver;
import com.mxi.am.driver.web.inventory.inventorydetailspage.InventoryDetailsPageDriver;
import com.mxi.am.driver.web.inventory.inventorydetailspage.inventorydetailspanes.DetailsPaneDriver;
import com.mxi.am.driver.web.inventory.inventorydetailspage.inventorydetailspanes.historicalpanes.FlightPaneDriver;
import com.mxi.am.driver.web.inventory.inventorydetailspage.inventorydetailspanes.historicalpanes.UsagePaneDriver;
import com.mxi.am.driver.web.inventory.inventorydetailspage.inventorydetailspanes.historicalpanes.UsagePaneDriver.UsageRecords;
import com.mxi.am.driver.web.inventory.inventorysearchpage.InventorySearchPageDriver;
import com.mxi.am.driver.web.inventory.inventorysearchpage.inventorysearchpanes.InventoryFoundPaneDriver.InventorySearchResult;
import com.mxi.am.driver.web.usage.CreateUsageRecordPageDriver;
import com.mxi.mx.common.utils.DateUtils;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.runtime.java.guice.ScenarioScoped;


/**
 * Step definitions for the feature "Display Usage History of Tracked Inventory"
 *
 */
@ScenarioScoped
public class DisplayUsageHistoryOfTrackedInventoryStepDefinitions {

   // Pre-loaded data in DB.
   private static final String ENGINEER = "Engineer";
   private static final String INVENTORY_SEARCH = "Inventory Search";
   private static final String AIRCRAFT_PART_NUMBER = "ACFT_ASSY_PN1";
   private static final String AIRCRAFT_SERIAL_NUMBER = "DisplayUsageHistory-ACFT-SN1";
   private static final String TRACKED_PART_NUMBER = "E0000015";
   private static final String TRACKED_SERIAL_NUMBER = "XXX";
   private static final String TRACKED_INSTALLED_ON = "Aircraft Part 1 - OPER-9917";
   private static final String AIRCRAFT_REG_CODE = "OPER-9917";
   private static final String HOURS = "HOURS";
   private static final String CYCLES = "CYCLES";
   private static final String DEPARTURE_AIRPORT = "AIRPORT1";
   private static final String ARRIVAL_AIRPORT = "AIRPORT2";
   private static final String FLIGHT = "Flight";
   private static final String USAGE_RECORD = "Usage Record";

   // This date has to be in the window of 60 days on the inventory detailed page so that the flight
   // could be searched
   private static final String MANUFACTURED_DATE =
         DateUtils.toString( DateUtils.addDays( new Date(), -10 ), DateUtils.DEFAULT_DATE_FORMAT );

   // created usage details
   private static final int[] USAGE_DELTA = { 3, 4, 5, 6, 7 };
   private static final int NUMBER_USAGE_RECORDS = USAGE_DELTA.length;

   // expected usage details
   private static final BigDecimal[] EXPECTED_FLYINGHOURS_DELTA = { new BigDecimal( 7 ),
         new BigDecimal( 6 ), new BigDecimal( 5 ), new BigDecimal( 4 ), new BigDecimal( 3 ) };
   private static final BigDecimal[] EXPECTED_FLYINGHOURS_TSN = { new BigDecimal( 25 ),
         new BigDecimal( 18 ), new BigDecimal( 12 ), new BigDecimal( 7 ), new BigDecimal( 3 ) };
   private static final BigDecimal[] EXPECTED_CYCLES_DELTA = { new BigDecimal( 1 ),
         new BigDecimal( 0 ), new BigDecimal( 1 ), new BigDecimal( 0 ), new BigDecimal( 1 ) };
   private static final BigDecimal[] EXPECTED_CYCLES_TSN = { new BigDecimal( 3 ),
         new BigDecimal( 2 ), new BigDecimal( 2 ), new BigDecimal( 1 ), new BigDecimal( 1 ) };

   private final BigDecimal iAircraftDbId;
   private final BigDecimal iAircraftId;

   private List<Usages> iCreatedUsagesList = new ArrayList<Usages>();
   private List<Usages> iDisplayedUsagesList;
   private List<Usages> iUsagesList;
   private BigDecimal iInitialUsageHoursTSN;
   private BigDecimal iInitialUsageCyclesTSN;
   private String iUsagesStartDate;
   private int iNumberOfFlights;
   private int iNumberOfUsageRecords;

   @Inject
   private NavigationDriver iNavigationDriver;

   @Inject
   private InventorySearchPageDriver iInventorySearchPageDriver;

   @Inject
   private InventoryDetailsPageDriver iInventoryDetailsPageDriver;

   @Inject
   private EditFlightPageDriver iEditFlightPageDriver;

   @Inject
   private FlightDetailsPageDriver iFlightDetailsPageDriver;

   @Inject
   private CreateUsageRecordPageDriver iCreateUsageRecordPageDriver;


   @Inject
   public DisplayUsageHistoryOfTrackedInventoryStepDefinitions(
         InventoryQueriesDriver aInventoryQueriesDriver) {

      iAircraftDbId =
            aInventoryQueriesDriver.getByAircraftRegistrationCode( AIRCRAFT_REG_CODE ).getDbId();
      iAircraftId =
            aInventoryQueriesDriver.getByAircraftRegistrationCode( AIRCRAFT_REG_CODE ).getId();
   }


   @Given( "^a tracked inventory on a sub-assembly of an aircraft$" )
   public void aTrackedInventoryOnASubAssemblyOfAnAircraft() throws Throwable {

      // Aircraft and sub-assembly (engine) set up via data loading. Engine has tracked inventory.
      // Aircraft and tracked inventory set up in in C_RI_ATTACH.csv, C_RI_INVENTORY.csv and
      // C_RI_Inventory_Usages.csv
   }


   @Given( "^the tracked inventory has a TSN value$" )
   public void theTrackedInventoryHasATSNValue() throws Throwable {

      // Engine has tracked inventory.
      // Aircraft and engine set up in in C_RI_ATTACH.csv, C_RI_INVENTORY.csv and
      // C_RI_Inventory_Usages.csv

      /*-indent
       *
       *  In order to facilitate the test running multiple times
       *  without requiring a dB rebuild, the initial TSN usage
       *  is determined and later subtracted from the created usage records.
       *
       */
      displayTrackDetailsPage();
      DetailsPaneDriver lDetailsPaneDriver = iInventoryDetailsPageDriver.clickTabDetails();
      iInitialUsageHoursTSN =
            new BigDecimal( lDetailsPaneDriver.getInventoryCurrentUsage( HOURS, "TSN" ) );
      iInitialUsageCyclesTSN =
            new BigDecimal( lDetailsPaneDriver.getInventoryCurrentUsage( CYCLES, "TSN" ) );

      /*-indent
       *
       * If
       *    The test is run for first time in the dB, no prior records exist
       *       MANUFACTURED_DATE is used as the initial usages start date
       * Else
       *    Prior records exist
       *       Latest usage date is read and used as the initial usages start date
       *
       */
      if ( iInitialUsageHoursTSN.equals( new BigDecimal( 0 ) ) ) { /* No prior usage records */
         iUsagesStartDate = MANUFACTURED_DATE;
      } else { /* Prior usage records */
         UsagePaneDriver lUsagePaneDriver =
               iInventoryDetailsPageDriver.clickTabHistorical().clickTabUsage();
         iUsagesStartDate = lUsagePaneDriver.getRecordDateByIndex( 0 );
      }
   }


   @Given( "^the aircraft has historical flights and usage records$" )
   public void theAircraftHasHistoricalFlightsAndUsageRecordss() throws Throwable {

      /*-indent
       *
       * Flights and Usage Records are created by incrementing the UsagesStartDate by
       * one day for each subsequent record.
       *
       */
      Date lUsageDate = new SimpleDateFormat( "dd-MMM-yyyy" ).parse( iUsagesStartDate );

      // create flight and usage records
      for ( int lIndexUsageRecord =
            0; lIndexUsageRecord < NUMBER_USAGE_RECORDS; lIndexUsageRecord++ ) {
         lUsageDate = DateUtils.addDays( lUsageDate, 1 ); /* increment record date by 1 day */
         if ( lIndexUsageRecord % 2 == 0 ) { /* even create flight */
            iNumberOfFlights++;
            createHistoricFlight( lUsageDate, USAGE_DELTA[lIndexUsageRecord] );
         } else { /* odd create usage record */
            iNumberOfUsageRecords++;
            createHistoricUsageRecord( lUsageDate, USAGE_DELTA[lIndexUsageRecord] );
         }

      }

      // sort iCreatedUsagesList by date descending to align with data read into iUsagesList
      Collections.sort( iCreatedUsagesList );
      Collections.reverse( iCreatedUsagesList );
   }


   @When( "^the historical usage of the tracked inventory is displayed$" )
   public void theHistoricalUsageOfTheTrackedInventoryIsDisplayed() throws Throwable {

      // Navigate to tracked inventory details page and store current usages
      displayTrackDetailsPage();
      DetailsPaneDriver lDetailsPaneDriver = iInventoryDetailsPageDriver.clickTabDetails();

      BigDecimal lCurrentUsageHoursTsn =
            new BigDecimal( lDetailsPaneDriver.getInventoryCurrentUsage( HOURS, "TSN" ) );
      BigDecimal lCurrentUsageCyclesTsn =
            new BigDecimal( lDetailsPaneDriver.getInventoryCurrentUsage( CYCLES, "TSN" ) );

      // Navigate to tracked inventory historical usages page and store usages
      iDisplayedUsagesList = getDisplayedUsagesList();

      // Adjust iDisplayedUsagesList by subtracting initial TSN usages
      iUsagesList = getAdjustedUsagesList();

      // Assert initial usages TSN's against tracked inventory usages
      Assert.assertEquals( "Unexpected Tracked Inventory Flying Hours Usage",
            iDisplayedUsagesList.get( 0 ).getFlyingHoursTsn(), lCurrentUsageHoursTsn );
      Assert.assertEquals( "Unexpected Tracked Inventory Cycles Usage",
            iDisplayedUsagesList.get( 0 ).getCyclesTsn(), lCurrentUsageCyclesTsn );
   }


   @Then( "^all the usages are observed$" )
   public void allTheUsagesAreObserved() throws Throwable {

      // Assert number of created flights against observed
      Assert.assertEquals( "Unexpected Number of Flights", iNumberOfFlights,
            getNumberOfCreatedUsages( FLIGHT ) );

      // Assert number of created usage records against observed
      Assert.assertEquals( "Unexpected Number of Usage Records", iNumberOfUsageRecords,
            getNumberOfCreatedUsages( USAGE_RECORD ) );

      // Assert total number of created usages against observed
      Assert.assertEquals( "Unexpected Number of Usage Records", NUMBER_USAGE_RECORDS,
            getNumberOfCreatedUsages( FLIGHT, USAGE_RECORD ) );
   }


   @Then( "^for each flight and usage record the Delta value of the tracked inventory equals the Delta value$" )
   public void forEachFlightAndUsageRecordTheDeltaValueOfTheTrackedInventoryEqualsTheDeltaValue()
         throws Throwable {

      for ( Usages lRecord : iUsagesList ) {

         int lIndex = iUsagesList.indexOf( lRecord );

         if ( lRecord.iRecordType.equals( USAGE_RECORD ) ) {

            // assert usage record Delta
            Assert.assertEquals( "Unexpected Usage Record Flying Hours Delta",
                  EXPECTED_FLYINGHOURS_DELTA[lIndex], lRecord.getFlyingHoursDelta() );
            Assert.assertEquals( "Unexpected Usage Record Cycles Delta",
                  EXPECTED_CYCLES_DELTA[lIndex], lRecord.getCyclesDelta() );
         }

         if ( lRecord.iRecordType.equals( FLIGHT ) ) {

            // assert flight Delta
            Assert.assertEquals( "Unexpected Flight Flying Hours Delta",
                  EXPECTED_FLYINGHOURS_DELTA[lIndex], lRecord.getFlyingHoursDelta() );
            Assert.assertEquals( "Unexpected Flight Record Cycles Delta",
                  EXPECTED_CYCLES_DELTA[lIndex], lRecord.getCyclesDelta() );
         }
      }
   }


   @Then( "^for each flight and usage record the TSN value equals the delta value plus the TSN value of the tracked inventory prior to the usage$" )
   public void
         forEachFlightAndUsageRecordTheTSNValueEqualsTheDeltaValuePlusTheTSNValueOfTheTrackedInventoryPriorToTheUsage()
               throws Throwable {

      for ( Usages lRecord : iUsagesList ) {

         int lIndex = iUsagesList.indexOf( lRecord );

         if ( lRecord.iRecordType.equals( USAGE_RECORD ) ) {

            // assert usage record TSN
            Assert.assertEquals( "Unexpected Usage Record Flying Hours TSN",
                  EXPECTED_FLYINGHOURS_TSN[lIndex], lRecord.getFlyingHoursTsn() );
            Assert.assertEquals( "Unexpected Usage Record Cycles TSN", EXPECTED_CYCLES_TSN[lIndex],
                  lRecord.getCyclesTsn() );
         }

         // assert flight TSN
         if ( lRecord.iRecordType.equals( FLIGHT ) ) {

            Assert.assertEquals( "Unexpected Flight Flying Hours TSN",
                  EXPECTED_FLYINGHOURS_TSN[lIndex], lRecord.getFlyingHoursTsn() );
            Assert.assertEquals( "Unexpected Flight Cycles TSN", EXPECTED_CYCLES_TSN[lIndex],
                  lRecord.getCyclesTsn() );
         }
      }
   }


   public void createHistoricFlight( Date aDate, int aUsageDelta ) {

      Flight lFlight;
      Usages lUsages;

      displayAircraftDetailsPage();
      FlightPaneDriver lFlightPaneDriver =
            iInventoryDetailsPageDriver.clickTabHistorical().clickTabFlight();
      lFlight = createFlight( lFlightPaneDriver, aUsageDelta, aDate );
      lUsages = getFlightUsage( lFlight );
      iCreatedUsagesList.add( lUsages );
   }


   public void createHistoricUsageRecord( Date aDate, int aUsageDelta ) {

      UsageRecord lUsageRecord;
      Usages lUsages;

      displayAircraftDetailsPage();
      UsagePaneDriver lUsagePaneDriver =
            iInventoryDetailsPageDriver.clickTabHistorical().clickTabUsage();
      lUsageRecord = createUsageRecord( lUsagePaneDriver, aUsageDelta, aDate );
      lUsages = getUsageRecordUsage( lUsageRecord );
      iCreatedUsagesList.add( lUsages );
   }


   private Usages getUsageRecordUsage( UsageRecord aUsageRecord ) {

      Usages lUsages = new Usages();
      lUsages.setUsageName( aUsageRecord.getName() );
      try {
         lUsages.setDate(
               new SimpleDateFormat( "dd-MMM-yyyy" ).parse( aUsageRecord.getRecordDate() ) );
      } catch ( ParseException e ) {
         throw new RuntimeException( e );
      }
      return lUsages;
   }


   private Usages getFlightUsage( Flight aFlight ) {

      Usages lUsages = new Usages();
      lUsages.setUsageName( aFlight.getName() );
      try {
         lUsages.setDate( new SimpleDateFormat( "dd-MMM-yyyy" ).parse( aFlight.getArrivalDate() ) );
      } catch ( ParseException e ) {
         throw new RuntimeException( e );
      }
      return lUsages;
   }


   private int getNumberOfCreatedUsages( String aRecordType ) {

      int lNumberOfRecords = 0;
      for ( Usages lRecord : iUsagesList ) {
         int lIndex = iUsagesList.indexOf( lRecord );
         // assert usage type
         if ( lRecord.iRecordType.equals( aRecordType ) ) {
            // assert usage name
            if ( iCreatedUsagesList.get( lIndex ).getUsageName()
                  .equals( lRecord.getUsageName() ) ) {
               lNumberOfRecords++;
            }
         }
      }
      return lNumberOfRecords;
   }


   private int getNumberOfCreatedUsages( String aRecordType1, String aRecordType2 ) {

      int lNumberOfRecords = 0;
      for ( Usages lRecord : iUsagesList ) {
         int lIndex = iUsagesList.indexOf( lRecord );
         // assert usage type
         if ( lRecord.getRecordType().equals( aRecordType1 )
               || lRecord.getRecordType().equals( aRecordType2 ) ) {
            // assert usage name
            if ( iCreatedUsagesList.get( lIndex ).getUsageName()
                  .equals( lRecord.getUsageName() ) ) {
               lNumberOfRecords++;
            }
         }
      }
      return lNumberOfRecords;
   }


   /*
    * Loop through the tracked inventory historical usage pane and store usages in returned list
    * lDisplayedUsageList.
    */
   private List<Usages> getDisplayedUsagesList() {

      List<Usages> lDisplayedUsagesList = new ArrayList<Usages>();
      UsagePaneDriver lUsagePaneDriver =
            iInventoryDetailsPageDriver.clickTabHistorical().clickTabUsage();

      // Loop thru first NUMBER_USAGE_RECORDS records only, as these are the usages created during
      // test
      for ( int lNumberUsageRecord =
            0; lNumberUsageRecord < NUMBER_USAGE_RECORDS; lNumberUsageRecord++ ) {

         UsageRecords lRecord = lUsagePaneDriver.getRecordByIndex( lNumberUsageRecord );

         Usages lUsage = new Usages();
         lUsage.setUsageName( lRecord.getName() );
         lUsage.setRecordType( lRecord.getType() );
         lUsage.setFlyingHoursDelta(
               new BigDecimal( convertFlyingHours( lRecord.getHoursDelta() ) ) );
         lUsage.setFlyingHoursTsn( new BigDecimal( convertFlyingHours( lRecord.getHoursTsn() ) ) );
         lUsage.setCyclesDelta( new BigDecimal( lRecord.getCyclesDelta() ) );
         lUsage.setCyclesTsn( new BigDecimal( lRecord.getCyclesTsn() ) );
         lDisplayedUsagesList.add( lUsage );
      }
      return lDisplayedUsagesList;
   }


   private int convertFlyingHours( String aFlyingHours ) {
      String[] lhourMin = aFlyingHours.split( ":" );
      int lhour = Integer.parseInt( lhourMin[0] );
      return lhour;
   }


   /*
    * Loop through DisplayedUsagesList and subtract initial TSN contributions from previous records
    */
   private List<Usages> getAdjustedUsagesList() {
      List<Usages> lAdjustedUsagesList = new ArrayList<Usages>();
      for ( Usages lRecord : iDisplayedUsagesList ) {
         Usages lUsage = new Usages();
         lUsage.setUsageName( lRecord.getUsageName() );
         lUsage.setRecordType( lRecord.getRecordType() );
         lUsage.setFlyingHoursDelta( lRecord.getFlyingHoursDelta() );
         lUsage.setFlyingHoursTsn( lRecord.getFlyingHoursTsn().subtract( iInitialUsageHoursTSN ) );
         lUsage.setCyclesDelta( lRecord.getCyclesDelta() );
         lUsage.setCyclesTsn( lRecord.getCyclesTsn().subtract( iInitialUsageCyclesTSN ) );
         lAdjustedUsagesList.add( lUsage );
      }
      return lAdjustedUsagesList;
   }


   private void displayAircraftDetailsPage() {
      iNavigationDriver.navigate( ENGINEER, INVENTORY_SEARCH );
      iInventorySearchPageDriver.clearAll();
      iInventorySearchPageDriver.setOEMPartNo( AIRCRAFT_PART_NUMBER );
      iInventorySearchPageDriver.setSerialNoBatchNo( AIRCRAFT_SERIAL_NUMBER );
      iInventorySearchPageDriver.clickSearch();

      List<InventorySearchResult> lResults =
            iInventorySearchPageDriver.clickTabInventoryFound().getResults();
      for ( InventorySearchResult lResult : lResults ) {
         if ( AIRCRAFT_PART_NUMBER.equals( lResult.getOemPartNo() )
               && AIRCRAFT_SERIAL_NUMBER.equals( lResult.getSerialNoBatchNo() ) ) {
            lResult.clickSerialNoBatchNo();
            return;
         }
      }
      throw new RuntimeException( "Aircraft Inventory not found " );
   }


   private void displayTrackDetailsPage() {
      iNavigationDriver.navigate( ENGINEER, INVENTORY_SEARCH );
      iInventorySearchPageDriver.clearAll();
      iInventorySearchPageDriver.setOEMPartNo( TRACKED_PART_NUMBER );
      iInventorySearchPageDriver.setSerialNoBatchNo( TRACKED_SERIAL_NUMBER );
      iInventorySearchPageDriver.setHighestInventory( TRACKED_INSTALLED_ON );
      iInventorySearchPageDriver.clickSearch();
      List<InventorySearchResult> lResults =
            iInventorySearchPageDriver.clickTabInventoryFound().getResults();
      for ( InventorySearchResult lResult : lResults ) {
         if ( TRACKED_PART_NUMBER.equals( lResult.getOemPartNo() )
               && TRACKED_SERIAL_NUMBER.equals( lResult.getSerialNoBatchNo() )
               && TRACKED_INSTALLED_ON.equals( lResult.getInstalledOn() ) ) {
            lResult.clickSerialNoBatchNo();
            return;
         }
      }
      throw new RuntimeException( "Tracked Inventory not found " );
   }


   private UsageRecord createUsageRecord( UsagePaneDriver aUsagePaneDriver, int aDelta,
         Date aCollectionDate ) {

      // Generate a unique usage record for each scenario.
      UsageRecord lUsageRecord =
            new UsageRecord().generateUniqueUsageRecord( aCollectionDate, aDelta );

      aUsagePaneDriver.clickCreateUsageRecord();

      // Create a historical usage record for the aircraft.
      // (this automatically generates HOURS usage values)
      iCreateUsageRecordPageDriver.setRecordName( lUsageRecord.getName() );
      iCreateUsageRecordPageDriver.setRecordDate( lUsageRecord.getRecordDate() );
      iCreateUsageRecordPageDriver.setRecordTime( lUsageRecord.getRecordTime() );
      iCreateUsageRecordPageDriver.setDeltaUsageByID(
            new BigDecimal( lUsageRecord.getUsageRecordDuration() ), HOURS, iAircraftDbId,
            iAircraftId );
      iCreateUsageRecordPageDriver.clickOK();
      return lUsageRecord;
   }


   private Flight createFlight( FlightPaneDriver aFlightPaneDriver, int aDelta, Date aDeparture ) {

      // Generate a unique flight information for each scenario.
      Flight lFlight = new Flight().generateUniqueFlight( aDelta, aDeparture );

      // Create a historical flight for the aircraft.
      // (this automatically generates HOURS and CYCLES usage values)
      aFlightPaneDriver.clickCreateFlight();
      iEditFlightPageDriver.setFlightName( lFlight.getName() );
      iEditFlightPageDriver.setDepartureAirport( ARRIVAL_AIRPORT );
      iEditFlightPageDriver.setArrivalAirport( DEPARTURE_AIRPORT );
      iEditFlightPageDriver.setActualDepartureDate( lFlight.getDepartureDate() );
      iEditFlightPageDriver.setActualDepartureTime( lFlight.getDepartureTime() );
      iEditFlightPageDriver.setActualArrivalDate( lFlight.getArrivalDate() );
      iEditFlightPageDriver.setActualArrivalTime( lFlight.getArrivalTime() );
      iEditFlightPageDriver.clickOK();
      iFlightDetailsPageDriver.clickClose();
      return lFlight;
   }


   private class UsageRecord {

      private String iName;
      private Date iDate;
      private int iUsageRecordDuration;


      public UsageRecord generateUniqueUsageRecord( Date aDate, int aDelta ) {

         // Use the current time's milliseconds as a unique value.
         Integer lRandomValue = new Integer( new SimpleDateFormat( "SSS" ).format( new Date() ) );
         iName = "UsageRecord" + lRandomValue;
         iUsageRecordDuration = aDelta;
         iDate = DateUtils.addMinutes( aDate, lRandomValue );

         return this;
      }


      public String getName() {
         return iName;
      }


      public int getUsageRecordDuration() {
         return iUsageRecordDuration;
      }


      public String getRecordDate() {
         String lDate = new SimpleDateFormat( "dd-MMM-yyyy" ).format( iDate );
         return lDate;
      }


      public String getRecordTime() {
         String lTime = new SimpleDateFormat( "HH:mm" ).format( iDate );
         return lTime;
      }

   }

   private class Flight {

      private String iName;
      private Date iDepartureDate;
      private Date iArrivalDate;
      private int iFlightDuration;


      public Flight generateUniqueFlight( int aDelta, Date aDepartureDate ) {

         // Use the current time's milliseconds as a unique value.
         Integer lRandomValue = new Integer( new SimpleDateFormat( "SSS" ).format( new Date() ) );
         iName = "Flight" + lRandomValue;
         iFlightDuration = aDelta;
         iDepartureDate = DateUtils.addMinutes( aDepartureDate, lRandomValue );
         iArrivalDate = DateUtils.addHours( iDepartureDate, iFlightDuration );

         return this;
      }


      public String getName() {
         return iName;
      }


      public String getDepartureDate() {
         return new SimpleDateFormat( "dd-MMM-yyyy" ).format( iDepartureDate );
      }


      public String getDepartureTime() {
         return new SimpleDateFormat( "HH:mm" ).format( iDepartureDate );
      }


      public String getArrivalDate() {
         return new SimpleDateFormat( "dd-MMM-yyyy" ).format( iArrivalDate );
      }


      public String getArrivalTime() {
         return new SimpleDateFormat( "HH:mm" ).format( iArrivalDate );
      }
   }

   private class Usages implements Comparable<Usages> {

      private String iUsageName;
      private String iRecordType;
      private Date iDate;
      private BigDecimal iFlyingHoursDelta;
      private BigDecimal iFlyingHoursTsn;
      private BigDecimal iCyclesDelta;
      private BigDecimal iCyclesTsn;


      public Usages() {
      }


      @Override
      public int compareTo( Usages aUsage ) {
         return getDate().compareTo( aUsage.getDate() );
      }


      public void setUsageName( String aUsageName ) {
         iUsageName = aUsageName;
      }


      public String getUsageName() {
         return iUsageName;
      }


      public void setRecordType( String aRecordType ) {
         iRecordType = aRecordType;
      }


      public String getRecordType() {
         return iRecordType;
      }


      public void setDate( Date aDate ) {
         iDate = aDate;
      }


      public Date getDate() {
         return iDate;
      }


      public void setFlyingHoursDelta( BigDecimal aFlyingHoursDelta ) {
         iFlyingHoursDelta = aFlyingHoursDelta;
      }


      public BigDecimal getFlyingHoursDelta() {
         return iFlyingHoursDelta;
      }


      public void setFlyingHoursTsn( BigDecimal aFlyingHoursTsn ) {
         iFlyingHoursTsn = aFlyingHoursTsn;
      }


      public BigDecimal getFlyingHoursTsn() {
         return iFlyingHoursTsn;
      }


      public void setCyclesDelta( BigDecimal aCyclesDelta ) {
         iCyclesDelta = aCyclesDelta;
      }


      public BigDecimal getCyclesDelta() {
         return iCyclesDelta;
      }


      public void setCyclesTsn( BigDecimal aCyclesTsn ) {
         iCyclesTsn = aCyclesTsn;
      }


      public BigDecimal getCyclesTsn() {
         return iCyclesTsn;
      }
   }
}
