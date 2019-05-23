package com.mxi.am.stepdefn.api.resource.materials.proc.vendor;

import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.Assert;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mxi.am.api.resource.ObjectMapperFactory;
import com.mxi.am.api.resource.materials.proc.vendor.Vendor;
import com.mxi.am.api.resource.materials.proc.vendor.Vendor.Contact;
import com.mxi.am.api.resource.materials.proc.vendor.Vendor.ShippingAddress;
import com.mxi.am.api.resource.materials.proc.vendor.Vendor.VendorOrgApproval;
import com.mxi.am.api.resource.materials.proc.vendor.Vendor.VendorOrgApproval.Approval;
import com.mxi.am.api.resource.organization.Organization;
import com.mxi.am.driver.integrationtesting.Rest;
import com.mxi.am.driver.integrationtesting.RestDriver;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;


/**
 * Update Vendor API Step Definitions
 *
 */
public class UpdateVendorAPIStepDefinitions {

   /* Authentication Constants */
   private static final String AMAPI = "/amapi/";

   /* Variables */
   private static final String TIME_ZONE = "timeZone";
   private static final String ID = "id";
   private static final String CURRENCY_CODE = "currencyCode";
   private static final String CODE = "code";
   private static final String NOTES = "notes";
   private static final String VENDOR_NAME = "vendorName";

   /* Contacts */
   private static final String NAME = "name";
   private static final String FAX_NO = "faxNumber";
   private static final String JOB_TITLE = "jobTitle";
   private static final String EMAIL_ADDRESS = "emailAddress";
   private static final String PHONE_NO = "phoneNumber";

   /* Variables */
   private static final String LOCATION_ID = "locationId";
   private static final String VENDOR_TYPE_CODE = "vendorTypeCode";
   private static final String ORG_CODE = "organizationCode";
   private static final String CERTIFICATE_NUMBER = "certificateNumber";
   private static final String CERT_EXP_DATE = "certificateExpiryDate";
   private static final String APPROV_TYPE_CODE = "approvalTypeCode";
   private static final String TERMS_AND_CONDS = "termsAndConds";
   private static final String SPEC2000_ENABLED = "spec2000Enabled";
   private static final String MIN_PURCH_AMT = "minPurchaseAmount";
   private static final String EXTERNAL_KEY = "externalKey";
   private static final String STD_BORROW_RATE = "stdBorrowRate";
   private static final String DEFAULT_AIRPORT = "defaultAirport";
   private static final String NOTE_TO_RECEIVER = "noteToReceiver";

   /* Shipping Address */
   private static final String ADD_1 = "address1";
   private static final String ADD_2 = "address2";
   private static final String CITY = "city";
   private static final String COUNTRY = "country";
   private static final String STATE = "state";
   private static final String ZIP = "zip";

   /* Vendor Org Approvals */
   private static final String STATUS = "UNAPPRVD";
   private static final String ORDER = "ORDER";
   private static final String SERVICE = "SERVICE";
   private static final String BORROW = "BORROW";
   private static final String CONSIGN = "CONSIGN";
   private static final String CSGNXCHG = "CSGNXCHG";
   private static final String EXCHANGE = "EXCHANGE";
   private static final String PURCHASE = "PURCHASE";
   private static final String REPAIR = "REPAIR";
   private static final String INSPECTION = "INSPECTION";
   private static final String MOD = "MOD";
   private static final String OVERHAUL = "OVERHAUL";
   private static final String TEST = "TEST";

   /* Message */
   private static final String MESSAGE = "message";

   @Inject
   @Rest
   private RestDriver iRestDriver;

   public static Vendor iVendor;
   public static boolean iSpec2000Enabled;
   public static Response iResponse;
   public static JsonObject iJsonResponse;
   public static String iVendorResponseTypeString;


   @When( "^an Update Vendor API request is sent to Maintenix$" )
   public void anUpdateVendorMessageIsSent( List<Map<String, String>> aDataTable )
         throws Throwable {
      // Retrieve the request data from the feature file
      Map<String, String> aTableRow = aDataTable.get( 0 );

      // We want response as JSON hence MediaType.APPLICATION_JSON
      Response lOrganizationResponse =
            iRestDriver.target( AMAPI + Organization.PATH + "?code=" + aTableRow.get( ORG_CODE ) )
                  .request().accept( MediaType.APPLICATION_JSON ).get();

      String lOrganizationResponseTypeString = lOrganizationResponse.readEntity( String.class );

      // Convert to JSON for parsing
      StringReader lOrganizationReader = new StringReader( lOrganizationResponseTypeString );
      JsonReader lOrganizationJsonReader = Json.createReader( lOrganizationReader );
      iJsonResponse = lOrganizationJsonReader.readArray().getJsonObject( 0 );
      lOrganizationJsonReader.close();
      String lOrgId = iJsonResponse.getString( ID );

      // We want response as JSON hence MediaType.APPLICATION_JSON
      Response lCreateVendorResponse =
            iRestDriver.target( AMAPI + Vendor.PATH + "?code=" + aTableRow.get( CODE ) ).request()
                  .accept( MediaType.APPLICATION_JSON ).get();

      String lCreateVendorResponseTypeString = lCreateVendorResponse.readEntity( String.class );

      // Convert to JSON for parsing
      StringReader lCreateVendorReader = new StringReader( lCreateVendorResponseTypeString );
      JsonReader lCreateVendorJsonReader = Json.createReader( lCreateVendorReader );
      iJsonResponse = lCreateVendorJsonReader.readArray().getJsonObject( 0 );
      lCreateVendorJsonReader.close();
      String lVendorId = iJsonResponse.getString( ID );
      String lLocationId = iJsonResponse.getString( LOCATION_ID );

      iVendor = new Vendor();

      SimpleDateFormat lDateFormatter = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ss.SSSZ" );
      Date lExpireDate = lDateFormatter.parse( aTableRow.get( CERT_EXP_DATE ) );

      iVendor.setTimeZone( aTableRow.get( TIME_ZONE ) );
      iVendor.setId( lVendorId );
      iVendor.setCurrencyCode( aTableRow.get( CURRENCY_CODE ) );
      iVendor.setCode( aTableRow.get( CODE ) );
      iVendor.setNotes( aTableRow.get( NOTES ) );
      iVendor.setVendorName( aTableRow.get( VENDOR_NAME ) );

      List<Contact> lListOfContacts = new ArrayList<Contact>();
      Contact lContact = new Contact();
      lContact.setName( aTableRow.get( NAME ) );
      lContact.setFaxNumber( aTableRow.get( FAX_NO ) );
      lContact.setJobTitle( aTableRow.get( JOB_TITLE ) );
      lContact.setEmailAddress( aTableRow.get( EMAIL_ADDRESS ) );
      lContact.setPhoneNumber( aTableRow.get( PHONE_NO ) );
      lContact.setMainContact( true );
      lListOfContacts.add( lContact );
      iVendor.setContacts( lListOfContacts );

      iVendor.setLocationId( lLocationId );
      iVendor.setVendorTypeCode( aTableRow.get( VENDOR_TYPE_CODE ) );
      iVendor.setOrganizationCode( aTableRow.get( ORG_CODE ) );
      iVendor.setCertificateNumber( aTableRow.get( CERTIFICATE_NUMBER ) );
      iVendor.setCertificateExpiryDate( lExpireDate );
      iVendor.setApprovalTypeCode( aTableRow.get( APPROV_TYPE_CODE ) );
      iVendor.setTermsAndConds( aTableRow.get( TERMS_AND_CONDS ) );
      if ( aTableRow.get( SPEC2000_ENABLED ).equalsIgnoreCase( "true" ) ) {
         iSpec2000Enabled = true;
      }
      iVendor.setSpec2000Enabled( iSpec2000Enabled );
      iVendor.setMinPurchaseAmount( aTableRow.get( MIN_PURCH_AMT ) );
      iVendor.setExternalKey( aTableRow.get( EXTERNAL_KEY ) );
      iVendor.setStdBorrowRate( aTableRow.get( STD_BORROW_RATE ) );
      // Default Airport has a bug. See SWA-3228
      // iVendor.setDefaultAirport( aTableRow.get( DEFAULT_AIRPORT ) );
      iVendor.setNoteToReceiver( aTableRow.get( NOTE_TO_RECEIVER ) );

      iVendor.setShippingAddress( new ShippingAddress() );
      iVendor.getShippingAddress().setAddress1( aTableRow.get( ADD_1 ) );
      iVendor.getShippingAddress().setAddress2( aTableRow.get( ADD_2 ) );
      iVendor.getShippingAddress().setCity( aTableRow.get( CITY ) );
      iVendor.getShippingAddress().setCountry( aTableRow.get( COUNTRY ) );
      iVendor.getShippingAddress().setState( aTableRow.get( STATE ) );
      iVendor.getShippingAddress().setZip( aTableRow.get( ZIP ) );

      List<VendorOrgApproval> lVendorOrgApprovalList = new ArrayList<VendorOrgApproval>();
      List<Approval> lApprovalList = new ArrayList<Approval>();

      Approval lBorrow = new Approval();
      lBorrow.setStatus( STATUS );
      lBorrow.setApprovalCode( BORROW );
      lBorrow.setApprovalType( ORDER );
      lApprovalList.add( lBorrow );

      Approval lConsign = new Approval();
      lConsign.setStatus( STATUS );
      lConsign.setApprovalCode( CONSIGN );
      lConsign.setApprovalType( ORDER );
      lApprovalList.add( lConsign );

      Approval lCsgnxchg = new Approval();
      lCsgnxchg.setStatus( STATUS );
      lCsgnxchg.setApprovalCode( CSGNXCHG );
      lCsgnxchg.setApprovalType( ORDER );
      lApprovalList.add( lCsgnxchg );

      Approval lExchange = new Approval();
      lExchange.setStatus( STATUS );
      lExchange.setApprovalCode( EXCHANGE );
      lExchange.setApprovalType( ORDER );
      lApprovalList.add( lExchange );

      Approval lPurchase = new Approval();
      lPurchase.setStatus( STATUS );
      lPurchase.setApprovalCode( PURCHASE );
      lPurchase.setApprovalType( ORDER );
      lApprovalList.add( lPurchase );

      Approval lRepair = new Approval();
      lRepair.setStatus( STATUS );
      lRepair.setApprovalCode( REPAIR );
      lRepair.setApprovalType( ORDER );
      lApprovalList.add( lRepair );

      Approval lInspection = new Approval();
      lInspection.setStatus( STATUS );
      lInspection.setApprovalCode( INSPECTION );
      lInspection.setApprovalType( SERVICE );
      lApprovalList.add( lInspection );

      Approval lMod = new Approval();
      lMod.setStatus( STATUS );
      lMod.setApprovalCode( MOD );
      lMod.setApprovalType( SERVICE );
      lApprovalList.add( lMod );

      Approval lOverhaul = new Approval();
      lOverhaul.setStatus( STATUS );
      lOverhaul.setApprovalCode( OVERHAUL );
      lOverhaul.setApprovalType( SERVICE );
      lApprovalList.add( lOverhaul );

      Approval lTest = new Approval();
      lTest.setStatus( STATUS );
      lTest.setApprovalCode( TEST );
      lTest.setApprovalType( SERVICE );
      lApprovalList.add( lTest );

      VendorOrgApproval lVendorOrgApproval = new VendorOrgApproval();
      lVendorOrgApproval.setOrgId( lOrgId );
      lVendorOrgApproval.setApprovalList( lApprovalList );

      lVendorOrgApprovalList.add( lVendorOrgApproval );

      iVendor.setVendorOrgApprovals( lVendorOrgApprovalList );

      ObjectMapper lMapper = ObjectMapperFactory.DEFAULT_OBJECT_MAPPER_FACTORY.createObjectMapper();
      String lJsonVendor = lMapper.writerWithDefaultPrettyPrinter().writeValueAsString( iVendor );

      // We want response as JSON hence MediaType.APPLICATION_JSON
      iResponse = iRestDriver.target( AMAPI + Vendor.PATH + "/" + lVendorId ).request()
            .accept( MediaType.APPLICATION_JSON ).put( Entity.json( lJsonVendor ) );

      iVendorResponseTypeString = iResponse.readEntity( String.class );

      StringReader lReader = new StringReader( iVendorResponseTypeString );
      JsonReader lJsonReader = Json.createReader( lReader );
      iJsonResponse = lJsonReader.readObject();
      lJsonReader.close();
   }


   @Then( "^the vendor is updated with the provided information$" )
   public void theVendorIsCreatedInDbWithTheProvidedInformation(
         List<Map<String, String>> aDataTable ) throws Throwable {

      Map<String, String> aTableRow = aDataTable.get( 0 );

      if ( iResponse.getStatus() != 200 ) {
         String lErrorMessage = iJsonResponse.getString( MESSAGE );
         Assert.fail( "Unexpected http request. Received a " + iResponse.getStatus()
               + " with error message: " + lErrorMessage );
      }

      // We want response as JSON hence MediaType.APPLICATION_JSON
      Response lCreateVendorResponse =
            iRestDriver.target( AMAPI + Vendor.PATH + "?code=" + aTableRow.get( CODE ) ).request()
                  .accept( MediaType.APPLICATION_JSON ).get();

      String lCreateVendorResponseTypeString = lCreateVendorResponse.readEntity( String.class );

      // Convert to JSON for parsing
      StringReader lCreateVendorReader = new StringReader( lCreateVendorResponseTypeString );
      JsonReader lCreateVendorJsonReader = Json.createReader( lCreateVendorReader );
      iJsonResponse = lCreateVendorJsonReader.readArray().getJsonObject( 0 );
      lCreateVendorJsonReader.close();

      String lTimeZone = iJsonResponse.getString( TIME_ZONE );
      String lCurrencyCode = iJsonResponse.getString( CURRENCY_CODE );
      String lVendorCode = iJsonResponse.getString( CODE );
      String lNotes = iJsonResponse.getString( NOTES );
      String lVendorName = iJsonResponse.getString( VENDOR_NAME );
      String lVendorTypeCode = iJsonResponse.getString( VENDOR_TYPE_CODE );
      String lOrganizationCode = iJsonResponse.getString( ORG_CODE );
      String lCertficateNumber = iJsonResponse.getString( CERTIFICATE_NUMBER );
      String lCertExpDate = iJsonResponse.getString( CERT_EXP_DATE );
      String lApprovalTypeCode = iJsonResponse.getString( APPROV_TYPE_CODE );
      String lTermsAndCond = iJsonResponse.getString( TERMS_AND_CONDS );
      boolean lSpec2000Enabled = iJsonResponse.getBoolean( SPEC2000_ENABLED );
      String lMinPurchaseAmount = iJsonResponse.getString( MIN_PURCH_AMT );
      String lExtKey = iJsonResponse.getString( EXTERNAL_KEY );
      String lStdBorrowRate = iJsonResponse.getString( STD_BORROW_RATE );
      // Default Airport has a bug. See SWA-3228
      // String lDefaultAirport = iJsonResponse.getString( DEFAULT_AIRPORT );
      String lNoteToReceiver = iJsonResponse.getString( NOTE_TO_RECEIVER );

      JsonObject lContact = iJsonResponse.getJsonArray( "contacts" ).getJsonObject( 0 );
      String lName = lContact.getString( NAME );
      String lFaxNumber = lContact.getString( FAX_NO );
      String lJobTitle = lContact.getString( JOB_TITLE );
      String lEmailAddress = lContact.getString( EMAIL_ADDRESS );
      String lPhoneNumber = lContact.getString( PHONE_NO );

      JsonObject lShippingAddress = iJsonResponse.getJsonObject( "shippingAddress" );
      String lState = lShippingAddress.getString( STATE );
      String lCountry = lShippingAddress.getString( COUNTRY );
      String lAddress1 = lShippingAddress.getString( ADD_1 );
      String lAddress2 = lShippingAddress.getString( ADD_2 );
      String lCity = lShippingAddress.getString( CITY );

      Assert.assertEquals( aTableRow.get( TIME_ZONE ), lTimeZone );
      Assert.assertEquals( aTableRow.get( CURRENCY_CODE ), lCurrencyCode );
      Assert.assertEquals( aTableRow.get( CODE ), lVendorCode );
      Assert.assertEquals( aTableRow.get( NOTES ), lNotes );
      Assert.assertEquals( aTableRow.get( VENDOR_NAME ), lVendorName );
      Assert.assertEquals( aTableRow.get( VENDOR_TYPE_CODE ), lVendorTypeCode );
      Assert.assertEquals( aTableRow.get( ORG_CODE ), lOrganizationCode );
      Assert.assertEquals( aTableRow.get( CERTIFICATE_NUMBER ), lCertficateNumber );
      Assert.assertEquals( aTableRow.get( CERT_EXP_DATE ), lCertExpDate );
      Assert.assertEquals( aTableRow.get( APPROV_TYPE_CODE ), lApprovalTypeCode );
      Assert.assertEquals( aTableRow.get( TERMS_AND_CONDS ), lTermsAndCond );
      Assert.assertEquals( iSpec2000Enabled, lSpec2000Enabled );
      Assert.assertEquals( aTableRow.get( MIN_PURCH_AMT ), lMinPurchaseAmount );
      Assert.assertEquals( aTableRow.get( EXTERNAL_KEY ), lExtKey );
      Assert.assertEquals( aTableRow.get( STD_BORROW_RATE ), lStdBorrowRate );
      // Default Airport has a bug. See SWA-3228
      // Assert.assertEquals( aTableRow.get( DEFAULT_AIRPORT ), lDefaultAirport );
      Assert.assertEquals( aTableRow.get( NOTE_TO_RECEIVER ), lNoteToReceiver );

      Assert.assertEquals( aTableRow.get( NAME ), lName );
      Assert.assertEquals( aTableRow.get( FAX_NO ), lFaxNumber );
      Assert.assertEquals( aTableRow.get( JOB_TITLE ), lJobTitle );
      Assert.assertEquals( aTableRow.get( EMAIL_ADDRESS ), lEmailAddress );
      Assert.assertEquals( aTableRow.get( PHONE_NO ), lPhoneNumber );

      Assert.assertEquals( aTableRow.get( STATE ), lState );
      Assert.assertEquals( aTableRow.get( COUNTRY ), lCountry );
      Assert.assertEquals( aTableRow.get( ADD_1 ), lAddress1 );
      Assert.assertEquals( aTableRow.get( ADD_2 ), lAddress2 );
      Assert.assertEquals( aTableRow.get( CITY ), lCity );

   }


   @Then( "^the update vendor error message \"([^\"]*)\" is verified$" )
   public void aResponseWithTheErrorMessageIsReceived( String aErrorMessage ) throws Throwable {
      String lErrorMessage = iJsonResponse.getString( MESSAGE );
      Assert.assertTrue(
            "The expected error was: " + aErrorMessage + " The actual error was: " + lErrorMessage,
            lErrorMessage.contains( aErrorMessage ) );
   }
}
