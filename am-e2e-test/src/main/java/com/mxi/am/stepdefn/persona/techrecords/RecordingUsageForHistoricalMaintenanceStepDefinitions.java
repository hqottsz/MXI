package com.mxi.am.stepdefn.persona.techrecords;

import static com.mxi.mx.common.utils.DateUtils.DEFAULT_DATETIME_FORMAT;
import static com.mxi.mx.common.utils.DateUtils.absoluteDifferenceInMinutes;
import static com.mxi.mx.common.utils.DateUtils.addHours;
import static com.mxi.mx.common.utils.DateUtils.addMinutes;
import static com.mxi.mx.common.utils.DateUtils.parseString;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.junit.Assert;

import com.mxi.am.driver.query.AircraftQueriesDriver;
import com.mxi.am.driver.query.AircraftQueriesDriver.AircraftInfo;
import com.mxi.am.driver.query.WorkPackageQueriesDriver;
import com.mxi.am.driver.web.AuthenticationRequiredPageDriver;
import com.mxi.am.driver.web.NavigationDriver;
import com.mxi.am.driver.web.PleaseWaitPaneDriver;
import com.mxi.am.driver.web.common.TableRowDriver;
import com.mxi.am.driver.web.flight.EditFlightPageDriver;
import com.mxi.am.driver.web.flight.flightDetails.FlightDetailsPageDriver;
import com.mxi.am.driver.web.flight.flightDetails.flightPanes.FlightInformationPaneDriver;
import com.mxi.am.driver.web.inventory.InventorySelectionPageDriver;
import com.mxi.am.driver.web.inventory.inventorydetailspage.InventoryDetailsPageDriver;
import com.mxi.am.driver.web.inventory.inventorydetailspage.inventorydetailspanes.HistoricalPaneDriver;
import com.mxi.am.driver.web.inventory.inventorydetailspage.inventorydetailspanes.historicalpanes.FlightPaneDriver;
import com.mxi.am.driver.web.task.CreateOrEditCheckPageDriver;
import com.mxi.am.driver.web.task.CreateTaskFromDefinitionPageDriver;
import com.mxi.am.driver.web.task.PackageAndCompletePageDriver;
import com.mxi.am.driver.web.task.ScheduleCheckPageDriver;
import com.mxi.am.driver.web.task.TaskSelectionPageDriver;
import com.mxi.am.driver.web.task.CheckDetails.CheckDetailsPageDriver;
import com.mxi.am.driver.web.task.CheckDetails.CheckDetailsPanes.DetailsPaneDriver;
import com.mxi.am.driver.web.task.TaskDetails.TaskDetailsPageDriver;
import com.mxi.am.driver.web.task.TaskDetails.TaskDetailsPanes.HistoryPaneDriver;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.runtime.java.guice.ScenarioScoped;


/**
 *
 * Step definitions for the feature Recording Usage For Historical Maintenance.
 *
 *
 * Note regarding generating unique flights in order to make this test repeatable:
 *
 * <pre>
 *   Flights are uniquely identified using aircraft + departure airport + departure date-time *  *
 *   In order to make this test repeatable we will need to create unique flights. We will use a
 *   constant aircraft and a constant departure airport but generate a unique departure date-tim *
 * 
 *   The strategy will be to search for the last flight that exists for the aircraft and create new
 *   flights after that last flight. If no flights exist then create flights after the manufactured
 *   date of the aircra * .
 * 
 *   The manufactured date of the aircraft is far enough in the past that by using small intervals
 *   between flights we should be able to re-run this test many times without generating flights in
 *   the future.
 * </pre>
 *
 */
@ScenarioScoped
public class RecordingUsageForHistoricalMaintenanceStepDefinitions {

   @Inject
   private AircraftQueriesDriver iAircraftQueriesDriver;

   @Inject
   private NavigationDriver iNavigationDriver;

   @Inject
   WorkPackageQueriesDriver iWorkPackageQueriesDriver;

   @Inject
   private InventoryDetailsPageDriver iInventoryDetailsPageDriver;

   @Inject
   private EditFlightPageDriver iEditFlightPageDriver;

   @Inject
   private FlightDetailsPageDriver iFlightDetailsPageDriver;

   @Inject
   private CreateOrEditCheckPageDriver iCreateOrEditCheckPageDriver;

   @Inject
   private CheckDetailsPageDriver iCheckDetailsPageDriver;

   @Inject
   private ScheduleCheckPageDriver iScheduleCheckPageDriver;

   @Inject
   private TaskDetailsPageDriver iTaskDetailsPageDriver;

   @Inject
   private PackageAndCompletePageDriver iPackageAndCompletePageDriver;

   @Inject
   private AuthenticationRequiredPageDriver iAuthenticationRequiredPageDriver;

   @Inject
   private InventorySelectionPageDriver iInventorySelectionPageDriver;

   @Inject
   private TaskSelectionPageDriver iTaskSelectionPageDriver;

   @Inject
   private CreateTaskFromDefinitionPageDriver iCreateTaskFromDefinitionPageDriver;

   @Inject
   private PleaseWaitPaneDriver iPleaseWaitPaneDriver;

   // Refer to: am-e2e-test\src\main\data\baseline\C_PART_NO_MISC_INFO.csv
   private static final String ACFT1_PN = "ACFT_ASSY_PN2";

   // Refer to: am-e2e-test\src\main\data\baseline\C_AIRPORT_DEPT.csv
   private static final String DEPARTURE_AIRPORT = "AIRPORT1";
   private static final String ARRIVAL_AIRPORT = "AIRPORT2";

   // Refer to: am-e2e-test\src\main\data\baseline\C_LOC_AIRPORT.csv
   // (I think this automatically creates the line location under the airport location).
   private static final String LOCATION = "AIRPORT1/LINE";

   // Refer to:
   // am-e2e-test\src\main\data\baseline\C_REQ.csv
   private static final String TASK_DEFN_NAME = "AL_TASK_FOLLOW_CRT (AL_TASK_FOLLOW_CRT)";

   private static final int FLIGHT_DURATION_HOURS = 4;
   private static final String FLIGHT_NAME = "FLIGHT_NAME";
   private static final String WORKPACKAGE_NAME = "WORKPACKAGE_NAME";
   private static final int MAX_WAIT_TIME_IN_MS = 5 * 60 * 1000; // 5 minutes

   private final static String PASSWORD = "password";

   private AircraftInfo iAircraftInfo;

   private Date iFirstFlightDepartureDate;
   private Date iFirstFlightArrivalDate;
   private Date iSecondFlightDepartureDate;

   private String iTaskBarcode;
   private String iWorkPackageBarcode;


   @Inject
   public RecordingUsageForHistoricalMaintenanceStepDefinitions(
         AircraftQueriesDriver aAircraftQueriesDriver) {

      // The aircraft is loaded via the data loading tools. Refer to:
      // am-e2e-test\src\main\data\actuals\persona\techRecords\RecordingUsageForHistoricMaintenance\C_RI_INVENTORY.csv
      iAircraftQueriesDriver = aAircraftQueriesDriver;
   };


   @Given( "^an aircraft \"([^\"]*)\" with two historical flights$" )
   public void anAircraftRecUsgRNWithTwoHistoricalFlights( String aAircraftSerialNumber )
         throws Throwable {

      // Refer to:
      // am-e2e-test\src\main\data\actuals\persona\techRecords\RecordingUsageForHistoricMaintenance\C_RI_INVENTORY.csv
      iAircraftInfo = iAircraftQueriesDriver.getAircraftInfoByPartAndSerialNo( ACFT1_PN,
            aAircraftSerialNumber );

      String lAircraftBarcode = iAircraftInfo.getBarcode();
      Date lAircraftManufacturedDate = iAircraftInfo.getManufacturedDate();

      // Set up the Inventory Details / Historical tab to display events far enough in the past.
      iNavigationDriver.barcodeSearch( lAircraftBarcode );
      HistoricalPaneDriver lHistoricalPane = iInventoryDetailsPageDriver.clickTabHistorical();
      lHistoricalPane.setDayCount( "9999" );
      lHistoricalPane.clickDayCountOk();

      // Create first flight using a unique departure date (to make the flight unique).
      iFirstFlightDepartureDate =
            generateUniqueFlightDepartureDate( lAircraftBarcode, lAircraftManufacturedDate );
      createHistoricalFlight( lAircraftBarcode, iFirstFlightDepartureDate, FLIGHT_DURATION_HOURS );

      // Get the arrival date and the usage from the first flight.
      iFirstFlightArrivalDate =
            iFlightDetailsPageDriver.clickTabFlightInformation().getArrivalDateTimeAsDate();

      // Create a second flight couple hours after the first flight arrives.
      iSecondFlightDepartureDate = addHours( iFirstFlightArrivalDate, 2 );
      createHistoricalFlight( lAircraftBarcode, iSecondFlightDepartureDate, FLIGHT_DURATION_HOURS );
   }


   @When( "^I start a historic work package between those flights$" )
   public void iStartAHistoricWorkPackageBetweenThoseFlights() throws Throwable {

      iNavigationDriver.barcodeSearch( iAircraftInfo.getBarcode() );
      // Create a work package.
      iInventoryDetailsPageDriver.clickTabOpen().clickTabOpenWorkPackages()
            .clickCreateWorkPackage();
      iCreateOrEditCheckPageDriver.setName( WORKPACKAGE_NAME );
      iCreateOrEditCheckPageDriver.clickOK();

      iPleaseWaitPaneDriver.waitForPleaseWaitToClose( MAX_WAIT_TIME_IN_MS );

      // Get the work package's barcode.
      iWorkPackageBarcode = iCheckDetailsPageDriver.clickTabDetails().getBarcode();

      // Schedule the work package.
      iCheckDetailsPageDriver.clickScheduleWorkPackage();
      iScheduleCheckPageDriver.setScheduledLocation( LOCATION );
      iScheduleCheckPageDriver.clickOK();

      // Start the work package as a historic work package using an actual start date that is
      // between the flights.
      // (i.e. after the first flight's arrival and before the second flight's departure )
      Date lWpStartDate =
            pickDateTimeBetween( iFirstFlightArrivalDate, iSecondFlightDepartureDate );

      iCheckDetailsPageDriver.clickStartHistoricWorkPackage();
      iCheckDetailsPageDriver.setStartHistoricWorkPackageActualStartDate( lWpStartDate );
      iCheckDetailsPageDriver.clickStartHistoricWorkPackageOk();

      iPleaseWaitPaneDriver.waitForPleaseWaitToClose( MAX_WAIT_TIME_IN_MS );

   }


   @Then( "^the work package usage snapshot reflects the usage snapshot of the first flight$" )
   public void theWorkPackageUsageSnapshotReflectsTheUsageSnapshotOfTheFirstFlight()
         throws Throwable {

      // Get the work package's usages.
      iNavigationDriver.barcodeSearch( iWorkPackageBarcode );
      DetailsPaneDriver lDetailsPane = iCheckDetailsPageDriver.clickTabDetails();
      String lWpCyclesTsn = lDetailsPane.getAssemblyAccruedTsn( "Cycles ( CYCLES )" );
      String lWpHoursTsn = lDetailsPane.getAssemblyAccruedTsn( "Flying Hours ( HOURS )" );

      // Get the first flight's usages.
      iNavigationDriver.barcodeSearch( iAircraftInfo.getBarcode() );
      iInventoryDetailsPageDriver.clickTabHistorical().clickTabFlight().clickFlight( FLIGHT_NAME,
            iFirstFlightDepartureDate, iFirstFlightArrivalDate );

      FlightInformationPaneDriver lFlightInformationPane =
            iFlightDetailsPageDriver.clickTabFlightInformation();
      String lFirstFlighCyclesTsn = lFlightInformationPane
            .getAssemblyAccruedTsn( iAircraftInfo.getDescription(), "CYCLES (Cycles)" );
      String lFirstFlighHoursTsn = lFlightInformationPane
            .getAssemblyAccruedTsn( iAircraftInfo.getDescription(), "HOURS (Flying Hours)" );

      // Get the usages for the first flight.
      Assert.assertEquals( "Unexpected work package CYCLES TSN.", lFirstFlighCyclesTsn,
            lWpCyclesTsn );
      Assert.assertEquals( "Unexpected work package HOURS TSN.", lFirstFlighHoursTsn, lWpHoursTsn );
   }


   @Given( "^there exists an open task against the aircraft$" )
   public void thereExistsAnOpenTaskAgainstTheAircraft() throws Throwable {

      iNavigationDriver.barcodeSearch( iAircraftInfo.getBarcode() );
      iInventoryDetailsPageDriver.clickTabOpen().clickTabOpenTasks().clickCreateTask();

      iInventorySelectionPageDriver.selectInventoryFromTree( iAircraftInfo.getDescription() );

      iTaskSelectionPageDriver.clickCreateBasedOnTaskDefinition();
      iTaskSelectionPageDriver.clickTaskDefinitionByName( TASK_DEFN_NAME );
      iTaskSelectionPageDriver.clickOkForCreateTask();

      iCreateTaskFromDefinitionPageDriver.clickOk();

      // Note: AuthenticationRequiredPageDriver does not seem to work for this page, so
      // CreateTaskFromDefinitionPageDriver will manage its Authentication pop-up.
      iAuthenticationRequiredPageDriver.setPasswordAndClickOK( PASSWORD );

      iTaskBarcode = iTaskDetailsPageDriver.getBarcode();
   }


   @When( "^I package and complete the task between those flights$" )
   public void iPackageAndCompleteTheTaskBetweenThoseFlights() throws Throwable {

      iNavigationDriver.barcodeSearch( iTaskBarcode );
      iTaskDetailsPageDriver.clickPackageAndCompleteTask();

      // Pick a completion date that is between the flights.
      // (i.e. after the first flight's arrival and before the second flight's departure )
      Date lCompletionDate =
            pickDateTimeBetween( iFirstFlightArrivalDate, iSecondFlightDepartureDate );
      iPackageAndCompletePageDriver.setCompletionDateTime( lCompletionDate );
      iPackageAndCompletePageDriver.setRepairLocation( LOCATION );
      iPackageAndCompletePageDriver.clickOk();

      iAuthenticationRequiredPageDriver.setPasswordAndClickOK( PASSWORD );
   }


   @Then( "^the usage snapshot of the package and completed task reflects the usage snapshot of the first flight$" )
   public void
         theUsageSnapshotOfThePackageAndCompletedTaskReflectsTheUsageSnapshotOfTheFirstFlight()
               throws Throwable {

      // Get usage snapshot of task.
      iNavigationDriver.barcodeSearch( iTaskBarcode );
      HistoryPaneDriver lHistoryPane = iTaskDetailsPageDriver.clickTabHistory();
      String lCyclesTsn = lHistoryPane.getUsageAtCompletion( iAircraftInfo.getDescription(),
            "Cycles ( CYCLES )", "TSN" );
      String lHoursTsn = lHistoryPane.getUsageAtCompletion( iAircraftInfo.getDescription(),
            "Flying Hours ( HOURS )", "TSN" );

      // Get the first flight's usages.
      iNavigationDriver.barcodeSearch( iAircraftInfo.getBarcode() );
      FlightPaneDriver lFlightPane =
            iInventoryDetailsPageDriver.clickTabHistorical().clickTabFlight();
      lFlightPane.clickFlight( FLIGHT_NAME, iFirstFlightDepartureDate, iFirstFlightArrivalDate );

      FlightInformationPaneDriver lFlightInformationPane =
            iFlightDetailsPageDriver.clickTabFlightInformation();
      String lFirstFlighCyclesTsn = lFlightInformationPane
            .getAssemblyAccruedTsn( iAircraftInfo.getDescription(), "CYCLES (Cycles)" );
      String lFirstFlighHoursTsn = lFlightInformationPane
            .getAssemblyAccruedTsn( iAircraftInfo.getDescription(), "HOURS (Flying Hours)" );

      Assert.assertEquals( "Unexpected CYCLES TSN for usage at completion of task.",
            lFirstFlighCyclesTsn, lCyclesTsn );
      Assert.assertEquals( "Unexpected HOURS TSN for usage at completion of task.",
            lFirstFlighHoursTsn, lHoursTsn );
   }


   private Date generateUniqueFlightDepartureDate( String aAircraftBarcode, Date aPreviousDate ) {

      // Get most recent flight for aircraft (if no flights use provided previous-date).
      Date lMostRecentDate = aPreviousDate;
      List<TableRowDriver> lFlightRows =
            iInventoryDetailsPageDriver.clickTabHistorical().clickTabFlight().getFlightRows();
      for ( TableRowDriver lFlightRow : lFlightRows ) {
         String lFlightArrivalDateStr = lFlightRow.getColumn( "Arrival.Actual Date" ).getText();

         Date lFlightDate;
         try {
            lFlightDate = parseString( lFlightArrivalDateStr, DEFAULT_DATETIME_FORMAT );
         } catch ( ParseException e ) {
            throw new RuntimeException( "Unable to parse flight arrival actual date string ["
                  + lFlightArrivalDateStr + "]" );
         }

         if ( lFlightDate.after( lMostRecentDate ) ) {
            lMostRecentDate = lFlightDate;
         }
      }

      // Add a couples hours to the most recent flight to make it unique.
      return addHours( lMostRecentDate, 2 );
   }


   private void createHistoricalFlight( String aAircraftBarcode, Date aDepartureDate,
         int aDurationHours ) {

      Date lArrivalDate = addHours( aDepartureDate, aDurationHours );

      iNavigationDriver.barcodeSearch( aAircraftBarcode );
      iInventoryDetailsPageDriver.clickTabHistorical().clickTabFlight().clickCreateFlight();

      // The "Create Flight" page is actually the EditFlight.jsp.
      iEditFlightPageDriver.setFlightName( FLIGHT_NAME );
      iEditFlightPageDriver.setArrivalAirport( DEPARTURE_AIRPORT );
      iEditFlightPageDriver.setDepartureAirport( ARRIVAL_AIRPORT );
      iEditFlightPageDriver.setActualDepartureDateAndTime( aDepartureDate );
      iEditFlightPageDriver.setActualArrivalDateAndTime( lArrivalDate );
      iEditFlightPageDriver.clickOK();
   }


   private Date pickDateTimeBetween( Date aFirstDate, Date aSecondDate ) {
      int lMinutesBetweenFlights = ( int ) absoluteDifferenceInMinutes( aFirstDate, aSecondDate );
      return addMinutes( aFirstDate, lMinutesBetweenFlights / 2 );
   }

}
