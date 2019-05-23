
package com.mxi.am.stepdefn.persona.techrecords;

import java.util.List;
import java.util.Map;

import javax.json.JsonObject;

import org.junit.Assert;

import com.google.inject.Inject;
import com.mxi.am.api.resource.materials.proc.vendor.Vendor;
import com.mxi.am.driver.integrationtesting.Rest;
import com.mxi.am.driver.integrationtesting.RestDriver;
import com.mxi.am.driver.web.NavigationDriver;
import com.mxi.am.driver.web.RefreshDriver;
import com.mxi.am.driver.web.vendor.ApprovalDetailsPageDriver;
import com.mxi.am.driver.web.vendor.ApprovalDetailsPageDriver.ApprovalStatus;
import com.mxi.am.driver.web.vendor.ApprovalDetailsPageDriver.UnapproveNote;
import com.mxi.am.driver.web.vendor.ApprovalDetailsPageDriver.UnapproveReason;
import com.mxi.am.driver.web.vendor.OrganizationDetailsPageDriver;
import com.mxi.am.driver.web.vendor.OrganizationSearchByTypePageDriver;
import com.mxi.am.driver.web.vendor.OrganizationSearchByTypePageDriver.Organizations;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;


/**
 * Theses are the step definitions for unapprove vendor feature file
 *
 */
public class UnapproveVendorStepDefinitions {

   /* Variables */
   private static final String PARENT_MENU_ITEM = "Parent_Menu_Item";
   private static final String CHILD_MENU_ITEM = "Child_Menu_Item";
   private static final String APP_DATE = "App_Date";
   private static final String TIME = "Time";
   private static final String STATUS = "Status";
   private static final String REASON = "Reason";
   private static final String NOTE = "Note";
   private static final String CODE = "Code";
   private static final String ORG_CODE = "Org_Code";

   @Inject
   private NavigationDriver iNavigationDriver;

   @Inject
   private OrganizationSearchByTypePageDriver iOrganizationSearchByTypePageDriver;

   @Inject
   private OrganizationDetailsPageDriver iOrganizationDetailsPageDriver;

   @Inject
   private RefreshDriver iRefreshDriver;

   @Inject
   private ApprovalDetailsPageDriver iApprovalDetailsPageDriver;

   @Inject
   @Rest
   private RestDriver iRestDriver;

   public static Vendor iVendor;
   public static JsonObject lJsonResponse;
   public static String iVendorResponseTypeString;


   @Given( "^I search for the organization page$" )
   // Navigate to the organization search by type page to organization page
   public void iAmOnTheOrganizationSearchByTypePage( List<Map<String, String>> aDataTable )
         throws Throwable {
      Map<String, String> aTableRow = aDataTable.get( 0 );

      iNavigationDriver.navigate( aTableRow.get( PARENT_MENU_ITEM ),
            aTableRow.get( CHILD_MENU_ITEM ) );

      iOrganizationSearchByTypePageDriver.setCode( aTableRow.get( ORG_CODE ) );
      iOrganizationSearchByTypePageDriver.search();

      List<Organizations> lOrganizationList = iOrganizationSearchByTypePageDriver.getResults();
      for ( Organizations lOrganizationResult : lOrganizationList ) {

         lOrganizationResult.selectOrganization();
      }
   }


   @Given( "^a vendor is approved in Maintenix$" )
   public void iAmOnTheOrganizationDetailsPage( List<Map<String, String>> aDataTable )
         throws Throwable {
      Map<String, String> aTableRow = aDataTable.get( 0 );

      // Navigate to the Approval Details Page by searching for the vendor and editing said vendors
      // approval details
      iOrganizationDetailsPageDriver.vendorList();
      iOrganizationDetailsPageDriver.setVendorCode( aTableRow.get( CODE ) );
      iOrganizationDetailsPageDriver.search();
      iOrganizationDetailsPageDriver.selectVendor();
      iOrganizationDetailsPageDriver.editApprovalDetails();

      // The vendor is approved for all order types
      iApprovalDetailsPageDriver.selectAll();
      iApprovalDetailsPageDriver.approveVendor();
      iApprovalDetailsPageDriver.setDate( aTableRow.get( APP_DATE ) );
      iApprovalDetailsPageDriver.setTime( aTableRow.get( TIME ) );
      iApprovalDetailsPageDriver.okButton();
   }


   @Then( "^the vendor is unapproved in Maintenix$" )
   // verify in the UI if the vendor has been unapproved
   public void theVendorIsUnapprovedInMaintenix( List<Map<String, String>> aDataTable )
         throws Throwable {
      iRefreshDriver.refreshCurrentPage();

      Map<String, String> aTableRow = aDataTable.get( 0 );

      List<ApprovalStatus> lApprovalStatusList = iApprovalDetailsPageDriver.getResults();
      for ( ApprovalStatus lApprovalStatusResult : lApprovalStatusList ) {
         Assert.assertEquals( aTableRow.get( STATUS ), lApprovalStatusResult.getApprovalStatus() );
      }

      List<UnapproveReason> lReasonList = iApprovalDetailsPageDriver.getReason();
      for ( UnapproveReason lReasonResult : lReasonList ) {
         Assert.assertEquals( aTableRow.get( REASON ), lReasonResult.getReason() );
      }

      List<UnapproveNote> lNoteList = iApprovalDetailsPageDriver.getNote();
      for ( UnapproveNote lNoteResult : lNoteList ) {
         Assert.assertEquals( aTableRow.get( NOTE ), lNoteResult.getNote() );
      }

   }

}
