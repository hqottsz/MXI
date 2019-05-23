package com.mxi.am.stepdefn.persona.techrecords;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.junit.Assert;

import com.mxi.am.driver.web.vendor.VendorDetailsPageDriver;
import com.mxi.am.driver.web.vendor.VendorSearchPageDriver;
import com.mxi.am.driver.web.vendor.vendorpanes.CommunicationPaneDriver;
import com.mxi.am.driver.web.vendor.vendorpanes.CommunicationPaneDriver.ContactRow;

import cucumber.api.java.en.Then;


/**
 * Step definitions for the update vendor feature.
 *
 */
public class UpdateVendorStepDefinitions {

   /* Variables */
   private static final String CODE = "Code";
   private static final String NAME = "Name";
   private static final String CERT_NO = "Cert_No";
   private static final String CERT_EXP_DATE = "Cert_Exp_Date";
   private static final String TYPE = "Type";
   private static final String APPROV_TYPE = "Approv_Type";
   private static final String TERMS_COND = "Terms_Cond";
   private static final String CURRENCY = "Currency";
   private static final String BORROW_RATE = "Borrow_Rate";
   private static final String TIME_ZONE = "Time_Zone";
   private static final String ADD_1 = "Add_1";
   private static final String CITY = "City";
   private static final String COUNTRY = "Country";
   private static final String STATE = "State";
   private static final String ZIP = "Zip";
   private static final String CONTACT_NAME = "Contact_Name";
   private static final String JOB_TITLE = "Job_Title";
   private static final String PHONE_NO = "Phone_No";
   private static final String FAX_NO = "Fax_No";
   private static final String EMAIL = "Email";

   @Inject
   private VendorDetailsPageDriver iVendorDetailsPageDriver;

   @Inject
   private VendorSearchPageDriver iVendorSearchPageDriver;

   @Inject
   private CommunicationPaneDriver iCommunicationPaneDriver;


   @Then( "^the vendor is updated in Maintenix$" )
   public void theVendorIsUpdatedInMaintenix( List<Map<String, String>> aDataTable )
         throws Throwable {
      // Retrieve the first row in the table
      Map<String, String> aTableRow = aDataTable.get( 0 );
      iVendorSearchPageDriver.clickClearAll();
      iVendorSearchPageDriver.search();
      iVendorSearchPageDriver.clickSearchResultRow( aTableRow.get( CODE ) );

      Assert.assertEquals( aTableRow.get( CODE ), iVendorDetailsPageDriver.getVendorCode() );
      Assert.assertEquals( aTableRow.get( NAME ), iVendorDetailsPageDriver.getVendorName() );

      Assert.assertEquals( aTableRow.get( CERT_NO ),
            iVendorDetailsPageDriver.getCertficateNumber() );
      Assert.assertEquals( aTableRow.get( TYPE ), iVendorDetailsPageDriver.getVendorType() );
      Assert.assertEquals( aTableRow.get( TERMS_COND ),
            iVendorDetailsPageDriver.getTermsAndConditions() );
      Assert.assertEquals( aTableRow.get( CURRENCY ), iVendorDetailsPageDriver.getCurrency() );
      Assert.assertEquals( aTableRow.get( TIME_ZONE ), iVendorDetailsPageDriver.getTimeZone() );
      Assert.assertEquals( aTableRow.get( CERT_EXP_DATE ),
            iVendorDetailsPageDriver.getCertificateExpiry() );
      Assert.assertEquals( aTableRow.get( APPROV_TYPE ),
            iVendorDetailsPageDriver.getApprovalType() );
      Assert.assertEquals( aTableRow.get( BORROW_RATE ),
            iVendorDetailsPageDriver.getStandardBorrowRate() );

      Assert.assertEquals( aTableRow.get( ADD_1 ), iVendorDetailsPageDriver.getAddress() );
      Assert.assertEquals( aTableRow.get( CITY ), iVendorDetailsPageDriver.getCity() );
      Assert.assertEquals( aTableRow.get( STATE ), iVendorDetailsPageDriver.getState() );
      Assert.assertEquals( aTableRow.get( COUNTRY ), iVendorDetailsPageDriver.getCountry() );
      Assert.assertEquals( aTableRow.get( ZIP ), iVendorDetailsPageDriver.getZip() );

      // Navigate to the communication pane
      iVendorDetailsPageDriver.clickTabCommunication();
      ContactRow lMainContact = iCommunicationPaneDriver.getMainContact();

      Assert.assertEquals( aTableRow.get( CONTACT_NAME ), lMainContact.getContactName() );
      Assert.assertEquals( aTableRow.get( JOB_TITLE ), lMainContact.getJobTitle() );
      Assert.assertEquals( aTableRow.get( PHONE_NO ), lMainContact.getPhone() );
      Assert.assertEquals( aTableRow.get( FAX_NO ), lMainContact.getFax() );
      Assert.assertEquals( aTableRow.get( EMAIL ), lMainContact.getEmail() );
   }
}
