
package com.mxi.am.stepdefn.persona.techrecords;

import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.time.DateUtils;
import org.junit.Assert;

import com.mxi.am.driver.common.ConfirmPageDriver;
import com.mxi.am.driver.web.AssetManagement;
import com.mxi.am.driver.web.NavigationDriver;
import com.mxi.am.driver.web.RefreshDriver;
import com.mxi.am.driver.web.common.UserAlertsPageDriver;
import com.mxi.am.driver.web.common.useralerts.UnassignedAlertsPaneDriver;
import com.mxi.am.driver.web.common.useralerts.UnassignedAlertsPaneDriver.AlertRow;
import com.mxi.am.driver.web.vendor.VendorDetailsPageDriver;
import com.mxi.am.driver.web.vendor.VendorDetailsPageDriver.HistoryRow;
import com.mxi.am.driver.web.vendor.VendorSearchPageDriver;
import com.mxi.driver.api.ApiDriver;
import com.mxi.driver.standard.Wait;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;


/**
 * Vendor Alerts Step Definitions
 *
 * @author jalarie
 */
public class VendorHistoryNotesAndUserAlertsStepDefintions {

   @Inject
   @AssetManagement
   private ApiDriver iApiDriver;

   @Inject
   private UserAlertsPageDriver iUserAlertsDriver;

   @Inject
   private NavigationDriver iNavigationDriver;

   @Inject
   private RefreshDriver iRefreshDriver;

   @Inject
   private ConfirmPageDriver iConfirmPageDriver;

   @Inject
   private VendorSearchPageDriver iVendorSearchPageDriver;

   @Inject
   private VendorDetailsPageDriver iVendorDetailsPageDriver;

   private static final int TIMEOUT_IN_SECONDS = 60 * 5;
   private static final int WAIT_IN_MILLISECONDS = 50;


   @Given( "^that a user has navigated to the user alerts page$" )
   public void navigateToUserAlerts( List<Map<String, String>> aDataTable ) throws Throwable {
      Map<String, String> aTableRow = aDataTable.get( 0 );
      iNavigationDriver.navigateOther( aTableRow.get( "Parent_Menu_Item" ),
            aTableRow.get( "Child_Menu_Item" ) );
   }


   @Given( "^that a user has deleted all notifications$" )
   public void allOldNticicationsAreDeleted() throws Throwable {
      UnassignedAlertsPaneDriver lUnassignedAlertsPaneDriver =
            iUserAlertsDriver.clickTabUnassignedAlerts();
      lUnassignedAlertsPaneDriver.selectAll();
      lUnassignedAlertsPaneDriver.clickDelete();
      iConfirmPageDriver.clickYes();
   }


   @When( "^the page is refreshed$" )
   public void thePageIsRefreshed() throws Throwable {
      iRefreshDriver.refreshCurrentPage();
   }


   @When( "^I navigate to the vendor history notes tab$" )
   public void iNaviateToTheVendorHistoryNotesTab( List<Map<String, String>> aDataTable )
         throws Throwable {
      // Retrieve the first row from the table
      Map<String, String> aTableRow = aDataTable.get( 0 );

      iNavigationDriver.navigate( aTableRow.get( "Parent_Menu_Item" ),
            aTableRow.get( "Child_Menu_Item" ) );

      iVendorSearchPageDriver.setVendorCode( aTableRow.get( "Code" ) );
      iVendorSearchPageDriver.clickClearAll();
      iVendorSearchPageDriver.search();
      iVendorSearchPageDriver.clickSearchResultRow( aTableRow.get( "Code" ) );

      iVendorDetailsPageDriver.clickTabHistory();

   }


   @Then( "^the appropriate history notes are logged$" )
   public void theApproprateHistoryNotesAreLogged( List<Map<String, String>> aDataTable )
         throws Throwable {

      Map<String, String> aTableRow = aDataTable.get( 0 );
      List<HistoryRow> lNotes = iVendorDetailsPageDriver.getNotes();
      boolean lCreate = false;
      boolean lUpdate = false;
      boolean lUnapprove = false;
      for ( int i = 0; i < lNotes.size(); i++ ) {

         if ( lNotes.get( i ).getAllNote()
               .equalsIgnoreCase( aTableRow.get( ( "Create_Vendor_Note" ) ) ) ) {
            lCreate = true;
         }
         if ( lNotes.get( i ).getAllNote()
               .equalsIgnoreCase( aTableRow.get( ( "Update_Vendor_Note" ) ) ) ) {
            lUpdate = true;
         }
         if ( lNotes.get( i ).getAllNote()
               .equalsIgnoreCase( aTableRow.get( ( "Unapprove_Vendor_Note" ) ) ) ) {
            lUnapprove = true;
         }
      }
      if ( !lCreate && !lUpdate && !lUnapprove ) {
         Assert.fail( "Hisotry Note not found" );
      }
   }


   @Then( "^the appropriate user alerts were logged$" )
   public void theApproprateUserAlertsAreLogged( List<Map<String, String>> aDataTable )
         throws Throwable {

      Map<String, String> aTableRow = aDataTable.get( 0 );
      List<AlertRow> lAlerts;
      Date lTimeout = DateUtils.addSeconds( new Date(), TIMEOUT_IN_SECONDS );
      // Wait until the Alert is loaded.
      do {
         lAlerts = iUserAlertsDriver.clickTabUnassignedAlerts().getUnassignedAlertLines();
         if ( CollectionUtils.isNotEmpty( lAlerts ) ) {
            Optional<AlertRow> lAlertFiltered = lAlerts.stream().filter( o -> ( o.getMessage() )
                  .equalsIgnoreCase( aTableRow.get( "Create_Vendor_Alert" ) )
                  || ( o.getMessage().equalsIgnoreCase( aTableRow.get( "Update_Vendor_Alert" ) ) )
                  || ( o.getMessage().equalsIgnoreCase( aTableRow.get( "Unapprove_Alert" ) ) ) )
                  .findAny();
            assertTrue( "Alert message not found", lAlertFiltered.isPresent() );
            return;
         }
         Wait.pause( WAIT_IN_MILLISECONDS );
         iNavigationDriver.refreshPage();
      } while ( ( new Date() ).before( lTimeout ) );

   }
}
