package com.mxi.am.stepdefn.persona.storeroomclerk;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.inject.Inject;

import org.junit.Assert;
import org.openqa.selenium.Keys;

import com.mxi.am.driver.query.InventoryQueriesDriver;
import com.mxi.am.driver.web.NavigationDriver;
import com.mxi.am.driver.web.inventory.inventorydetailspage.CheckInCheckOutHistoryPaneDriver;
import com.mxi.am.driver.web.inventory.inventorydetailspage.CheckInCheckOutHistoryPaneDriver.CheckInCheckOutHistoryTable;
import com.mxi.am.driver.web.inventory.inventorydetailspage.InventoryDetailsPageDriver;
import com.mxi.am.driver.web.todolist.ToDoListPageDriver;
import com.mxi.am.driver.web.todolist.ToDoListPanes.CheckedOutToolsPaneDriver;
import com.mxi.am.driver.web.tool.CheckOutToolPageDriver;
import com.mxi.driver.standard.Wait;
import com.mxi.mx.common.utils.DateUtils;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.runtime.java.guice.ScenarioScoped;


@ScenarioScoped
public class CheckOutCheckInToolStepDefinitions {

   @Inject
   private NavigationDriver iNavigationDriver;

   @Inject
   private ToDoListPageDriver iToDoListPageDriver;

   private CheckedOutToolsPaneDriver iCheckedOutToolsPaneDriver;

   @Inject
   private CheckOutToolPageDriver iCheckOutToolPageDriver;

   @Inject
   private InventoryDetailsPageDriver iInventoryDetailsPageDriver;

   private final String TOOL2_BARCODE;


   @Inject
   public CheckOutCheckInToolStepDefinitions(InventoryQueriesDriver aInventoryQueriesDriver) {
      TOOL2_BARCODE = aInventoryQueriesDriver.getBarcodeBySerialPartNo( PART_NO, TOOL2_SERIAL_NO );
   }


   private final static String PART_NO = "T0000005";
   private final static String TOOL1_SERIAL_NO = "TOOLCINCOUT";
   private final static String TOOL2_SERIAL_NO = "SUFETOOLSN1";
   private final static String EXPECTED_CHECK_OUT_EVENT = "OUT";
   private final static String EXPECTED_CHECK_IN_EVENT = "IN";
   private final static String USER = "User1";
   private final static String STOREROOM_CLERK = "Storeroom Clerk";
   private final static String STOREROOM_TO_DO_LIST = "To Do List (Storeroom Clerk)";
   private final static String CHECKOUT_HISTORY_NOTE = "This tool was checked out to User 1.";
   private final static String CHECKIN_HISTORY_NOTE =
         "This tool was checked in by User 1.The inventory condition was 'RFI'.";


   // Scenario 1 - Check out tools
   @When( "^a user checks out tools from the Storeroom Clerk$" )
   public void aUserChecksOutToolsFromTheStoreroomClerk() throws Throwable {

      // find the tool barcode
      iNavigationDriver.navigate( STOREROOM_CLERK, STOREROOM_TO_DO_LIST );
      iCheckedOutToolsPaneDriver = iToDoListPageDriver.clickTabCheckedOutTools();
      iCheckedOutToolsPaneDriver.clickCheckOutTool();

      // Data entry in the Tool Checkout form can be done two ways. Manually or using a barcode
      // scanner;
      // 1. All values including PN and SN are entered manually. All mandatory fields
      // need to be filled before clicking OK and Repeat
      iCheckOutToolPageDriver.setUser( USER );
      iCheckOutToolPageDriver.setPartNo( PART_NO );
      iCheckOutToolPageDriver.setSerialNo( TOOL1_SERIAL_NO );
      iCheckOutToolPageDriver.clickOkAndRepeatButton();

      // 2. When accessing the page for the second time (after OK and Repeat), the User field should
      // persist. When barcode is scanned (text input followed by EOL character ENTER), the PN and
      // SN should be fetched automatically.
      scanToolBarcode( TOOL2_BARCODE );
      // we have to change this from pressing Enter to click OK button because of inconsistency of
      // focus moving
      iCheckOutToolPageDriver.clickOKButton();
   }


   @Then( "^the tools appear under the Checked Out Tools tab$" )
   public void theToolsAppearUnderTheCheckedOutToolsTab() throws Throwable {
      Assert.assertTrue( "Tool was not in Checked Out Tools tab ",
            iCheckedOutToolsPaneDriver.isSerialInTable( TOOL1_SERIAL_NO ) );
      Assert.assertTrue( "Tool was not in Checked Out Tools tab ",
            iCheckedOutToolsPaneDriver.isSerialInTable( TOOL2_SERIAL_NO ) );

      // If the tool is visible in the Checked Out Tools Tab, ensure that the Expected Return Date
      // was set correctly. (The config parameter is set to 24 hours in the data setup - see
      // utl_config_parm.sql)
      Assert.assertEquals(
            formatExpectedReturnDate( DateUtils.addDays(
                  iCheckedOutToolsPaneDriver.getResults().get( 0 ).getCheckOutDateAsDate(), 1 ) ),
            formatExpectedReturnDate( iCheckedOutToolsPaneDriver.getResults().get( 0 )
                  .getExpectedReturnDateAsDate() ) );
   }


   @Then( "^the tools are marked as Checked Out$" )
   public void theToolsAreMarkedAsCheckedOut() throws Throwable {
      // Verify the Checked Out checkbox in the inventory details page is checked. Sufficient to
      // check for one tool.
      iCheckedOutToolsPaneDriver.clickSerialNumberInTable( TOOL1_SERIAL_NO );
      Assert.assertEquals( true, iInventoryDetailsPageDriver.clickTabDetails().getCheckedOut() );
   }


   @Then( "^a checked-out history note is added to the inventory$" )
   public void aCheckedOutHistoryNoteIsAddedToTheInventory() throws Throwable {
      // Verify that the Check Out event and history note are set
      assertCheckInCheckOutHistoryNote( EXPECTED_CHECK_OUT_EVENT, CHECKOUT_HISTORY_NOTE );
   }


   // Scenario 2 - Check in tools
   @When( "^a user checks in tools to the Storeroom Clerk$" )
   public void aUserChecksInAToolToTheStoreroomClerk() throws Throwable {

      iNavigationDriver.navigate( STOREROOM_CLERK, STOREROOM_TO_DO_LIST );
      iCheckedOutToolsPaneDriver = iToDoListPageDriver.clickTabCheckedOutTools();

      // Click the serial number and then Check In tool - PN, SN and Tool Barcode fields should be
      // populated
      Wait.pause( 4000 );
      iCheckedOutToolsPaneDriver.clickSerialNumberInTable( TOOL1_SERIAL_NO );
      iInventoryDetailsPageDriver.clickCheckInTool();
      iCheckOutToolPageDriver.setUser( USER );
      iCheckOutToolPageDriver.clickOkAndRepeatButton();

      // When accessing through OK and Repeat, the User should persist. When a Barcode is scanned
      // the new PN and SN should be populated.
      scanToolBarcode( TOOL2_BARCODE );
      // we have to change this from pressing Enter to click OK button because of inconsistency of
      // focus moving
      iCheckOutToolPageDriver.clickOKButton();

   }


   @Then( "^the tools are not marked as Checked Out$" )
   public void theToolIsNotMarkedAsCheckedOut() throws Throwable {
      // Verify the Checked Out checkbox in the inventory details page is not checked. Sufficient to
      // check for one tool.
      Assert.assertEquals( false, iInventoryDetailsPageDriver.clickTabDetails().getCheckedOut() );
   }


   @Then( "^a checked-in history note is added to the inventory$" )
   public void aCheckedInHistoryNoteIsAddedToTheInventory() throws Throwable {
      // Verify that the Check In event and history note are set
      assertCheckInCheckOutHistoryNote( EXPECTED_CHECK_IN_EVENT, CHECKIN_HISTORY_NOTE );

   }


   /**
    *
    * Asserts whether the history note and event are updated in the CheckInCheckOutHistory table
    *
    * @param aExpectedEventType
    *           - should be IN or OUT
    * @param aExpectedNote
    *           - expected history note for check in/out
    */
   private void assertCheckInCheckOutHistoryNote( String aExpectedEventType,
         String aExpectedNote ) {
      CheckInCheckOutHistoryPaneDriver lCheckInCheckOutHistoryPaneDriver =
            iInventoryDetailsPageDriver.clickTabCheckInCheckOutHistoryTab();

      CheckInCheckOutHistoryTable lCheckInCheckOutHistory =
            lCheckInCheckOutHistoryPaneDriver.getEventByNote( aExpectedNote );

      Assert.assertEquals( "Check In Check Out History Event not as expected.", aExpectedEventType,
            lCheckInCheckOutHistory.getEvent() );
   }


   /**
    * Formats dates to verify whether expected return date is correct
    */
   private String formatExpectedReturnDate( Date aDate ) {
      SimpleDateFormat lSimpleDateFormat = new SimpleDateFormat( "dd-MMM-yyyy" );
      return lSimpleDateFormat.format( aDate );
   }


   /**
    *
    * This method mimics scanning a tool barcode with a scanner, assuming that the scanner has the
    * 'enter' EOL character
    *
    * @param aBarcode
    *           - barcode derived from PN and SN of the inventory (tool)
    */
   private void scanToolBarcode( String aBarcode ) {
      iCheckOutToolPageDriver.setBarcode( aBarcode );
      iNavigationDriver.sendKeyToActiveElement( Keys.ENTER );
   }

}
