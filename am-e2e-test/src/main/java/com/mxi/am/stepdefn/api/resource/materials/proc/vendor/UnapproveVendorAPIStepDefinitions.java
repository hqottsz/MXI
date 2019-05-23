package com.mxi.am.stepdefn.api.resource.materials.proc.vendor;

import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.Assert;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.mxi.am.api.resource.ObjectMapperFactory;
import com.mxi.am.api.resource.materials.proc.vendor.Vendor;
import com.mxi.am.api.resource.materials.proc.vendor.Vendor.Contact;
import com.mxi.am.api.resource.materials.proc.vendor.Vendor.ShippingAddress;
import com.mxi.am.api.resource.materials.proc.vendor.Vendor.VendorOrgApproval;
import com.mxi.am.api.resource.materials.proc.vendor.Vendor.VendorOrgApproval.Approval;
import com.mxi.am.api.resource.organization.Organization;
import com.mxi.am.driver.integrationtesting.Rest;
import com.mxi.am.driver.integrationtesting.RestDriver;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;


/**
 * Unapprove Vendor API Step Definitions
 *
 */
public class UnapproveVendorAPIStepDefinitions {

   /* Authentication Constants */
   private static final String AMAPI = "/amapi/";

   /* Variables */
   private static final String ID = "id";
   private static final String LOCATION_ID = "locationId";
   private static final String CODE = "code";
   private static final String VENDOR_NAME = "vendorName";
   private static final String ORGANIZATION_CODE = "organizationCode";
   private static final String CERT_EXP_DATE = "certificateExpiryDate";
   private static final String VENDOR_TYPE_CODE = "vendorTypeCode";
   private static final String CURRENCY_CODE = "currencyCode";
   private static final String MIN_PURCHASE_AMOUNT = "minPurchaseAmount";
   private static final String EXTERNAL_KEY = "externalKey";
   private static final String TIME_ZONE = "timeZone";
   private static final String ADD_1 = "address1";
   private static final String CITY = "city";
   private static final String COUNTRY = "country";
   private static final String NAME = "name";

   /* Approval Status */
   private static final String STATUS = "status";
   private static final String REASON_CODE = "reasonCode";
   private static final String NOTE = "note";

   /* Approval Types */
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
   public static Response iResponse;
   public static JsonObject iJsonResponse;
   public static String iVendorResponseTypeString;


   @Given( "^an Update Vendor API request is sent to Maintenix to approve the vendor$" )
   public void aVendorIsApprovedInMaintenix( List<Map<String, String>> aDataTable )
         throws Throwable {
      anUnapproveVendorMessageIsSentToMaintenix( aDataTable );
   }


   @When( "^an Update Vendor API request is sent to Maintenix to unapprove the vendor$" )
   public void anUnapproveVendorMessageIsSentToMaintenix( List<Map<String, String>> aDataTable )
         throws Throwable {
      // Retrieve the request data from the feature file
      Map<String, String> aTableRow = aDataTable.get( 0 );

      // We want response as JSON hence MediaType.APPLICATION_JSON
      Response lOrganizationResponse =

            iRestDriver
                  .target(
                        AMAPI + Organization.PATH + "?code=" + aTableRow.get( ORGANIZATION_CODE ) )
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

      iVendor.setId( lVendorId );
      iVendor.setLocationId( lLocationId );
      iVendor.setCode( aTableRow.get( CODE ) );
      iVendor.setMinPurchaseAmount( aTableRow.get( MIN_PURCHASE_AMOUNT ) );
      iVendor.setExternalKey( aTableRow.get( EXTERNAL_KEY ) );
      iVendor.setVendorName( aTableRow.get( VENDOR_NAME ) );
      iVendor.setOrganizationCode( aTableRow.get( ORGANIZATION_CODE ) );
      iVendor.setCertificateExpiryDate( lExpireDate );

      iVendor.setVendorTypeCode( aTableRow.get( VENDOR_TYPE_CODE ) );
      iVendor.setCurrencyCode( aTableRow.get( CURRENCY_CODE ) );
      iVendor.setTimeZone( aTableRow.get( TIME_ZONE ) );

      iVendor.setShippingAddress( new ShippingAddress() );
      iVendor.getShippingAddress().setAddress1( aTableRow.get( ADD_1 ) );
      iVendor.getShippingAddress().setCity( aTableRow.get( CITY ) );
      iVendor.getShippingAddress().setCountry( aTableRow.get( COUNTRY ) );

      List<Contact> lListOfContacts = new ArrayList<Contact>();
      Contact lContact = new Contact();
      lContact.setName( aTableRow.get( NAME ) );
      lContact.setMainContact( true );
      lListOfContacts.add( lContact );
      iVendor.setContacts( lListOfContacts );

      List<VendorOrgApproval> lVendorOrgApprovalList = new ArrayList<VendorOrgApproval>();
      List<Approval> lApprovalList = new ArrayList<Approval>();
      Approval lBorrow = new Approval();
      lBorrow.setStatus( aTableRow.get( STATUS ) );
      lBorrow.setApprovalCode( BORROW );
      lBorrow.setApprovalType( ORDER );

      if ( aTableRow.get( STATUS ).equals( "UNAPPRVD" ) ) {
         lBorrow.setReasonCode( aTableRow.get( REASON_CODE ) );
         lBorrow.setNote( aTableRow.get( NOTE ) );
      } else {
         lBorrow.setApprovalExpiryDate( lExpireDate );
      }
      lApprovalList.add( lBorrow );

      Approval lConsign = new Approval();
      lConsign.setStatus( aTableRow.get( STATUS ) );
      lConsign.setApprovalCode( CONSIGN );
      lConsign.setApprovalType( ORDER );
      if ( aTableRow.get( STATUS ).equals( "UNAPPRVD" ) ) {
         lConsign.setReasonCode( aTableRow.get( REASON_CODE ) );
         lConsign.setNote( aTableRow.get( NOTE ) );
      } else {
         lConsign.setApprovalExpiryDate( lExpireDate );
      }
      lApprovalList.add( lConsign );

      Approval lCsgnxchg = new Approval();
      lCsgnxchg.setStatus( aTableRow.get( STATUS ) );
      lCsgnxchg.setApprovalCode( CSGNXCHG );
      lCsgnxchg.setApprovalType( ORDER );
      if ( aTableRow.get( STATUS ).equals( "UNAPPRVD" ) ) {
         lCsgnxchg.setReasonCode( aTableRow.get( REASON_CODE ) );
         lCsgnxchg.setNote( aTableRow.get( NOTE ) );
      } else {
         lCsgnxchg.setApprovalExpiryDate( lExpireDate );
      }
      lApprovalList.add( lCsgnxchg );

      Approval lExchange = new Approval();
      lExchange.setStatus( aTableRow.get( STATUS ) );
      lExchange.setApprovalCode( EXCHANGE );
      lExchange.setApprovalType( ORDER );
      if ( aTableRow.get( STATUS ).equals( "UNAPPRVD" ) ) {
         lExchange.setReasonCode( aTableRow.get( REASON_CODE ) );
         lExchange.setNote( aTableRow.get( NOTE ) );
      } else {
         lExchange.setApprovalExpiryDate( lExpireDate );
      }
      lApprovalList.add( lExchange );

      Approval lPurchase = new Approval();
      lPurchase.setStatus( aTableRow.get( STATUS ) );
      lPurchase.setApprovalCode( PURCHASE );
      lPurchase.setApprovalType( ORDER );
      if ( aTableRow.get( STATUS ).equals( "UNAPPRVD" ) ) {
         lPurchase.setReasonCode( aTableRow.get( REASON_CODE ) );
         lPurchase.setNote( aTableRow.get( NOTE ) );
      } else {
         lPurchase.setApprovalExpiryDate( lExpireDate );
      }
      lApprovalList.add( lPurchase );

      Approval lRepair = new Approval();
      lRepair.setStatus( aTableRow.get( STATUS ) );
      lRepair.setApprovalCode( REPAIR );
      lRepair.setApprovalType( ORDER );
      if ( aTableRow.get( STATUS ).equals( "UNAPPRVD" ) ) {
         lRepair.setReasonCode( aTableRow.get( REASON_CODE ) );
         lRepair.setNote( aTableRow.get( NOTE ) );
      } else {
         lRepair.setApprovalExpiryDate( lExpireDate );
      }
      lApprovalList.add( lRepair );

      Approval lInspection = new Approval();
      lInspection.setStatus( aTableRow.get( STATUS ) );
      lInspection.setApprovalCode( INSPECTION );
      lInspection.setApprovalType( SERVICE );
      if ( aTableRow.get( STATUS ).equals( "APPROVED" ) ) {
         lInspection.setApprovalExpiryDate( lExpireDate );
      }
      lApprovalList.add( lInspection );

      Approval lMod = new Approval();
      lMod.setStatus( aTableRow.get( STATUS ) );
      lMod.setApprovalCode( MOD );
      lMod.setApprovalType( SERVICE );
      if ( aTableRow.get( STATUS ).equals( "APPROVED" ) ) {
         lMod.setApprovalExpiryDate( lExpireDate );
      }
      lApprovalList.add( lMod );

      Approval lOverhaul = new Approval();
      lOverhaul.setStatus( aTableRow.get( STATUS ) );
      lOverhaul.setApprovalCode( OVERHAUL );
      lOverhaul.setApprovalType( SERVICE );
      if ( aTableRow.get( STATUS ).equals( "APPROVED" ) ) {
         lOverhaul.setApprovalExpiryDate( lExpireDate );
      }
      lApprovalList.add( lOverhaul );

      Approval lTest = new Approval();
      lTest.setStatus( aTableRow.get( STATUS ) );
      lTest.setApprovalCode( TEST );
      lTest.setApprovalType( SERVICE );
      if ( aTableRow.get( STATUS ).equals( "APPROVED" ) ) {
         lTest.setApprovalExpiryDate( lExpireDate );
      }
      lApprovalList.add( lTest );

      VendorOrgApproval lVendorOrgApproval = new VendorOrgApproval();
      lVendorOrgApproval.setApprovalList( lApprovalList );

      lVendorOrgApprovalList.add( lVendorOrgApproval );
      lVendorOrgApproval.setOrgId( lOrgId );
      iVendor.setVendorOrgApprovals( lVendorOrgApprovalList );

      ObjectMapper lMapper = ObjectMapperFactory.DEFAULT_OBJECT_MAPPER_FACTORY.createObjectMapper();
      String lJsonVendor = lMapper.writerWithDefaultPrettyPrinter().writeValueAsString( iVendor );

      // We want response as JSON hence MediaType.APPLICATION_JSON
      iResponse = iRestDriver.target( AMAPI + Vendor.PATH + "/" + lVendorId ).request()
            .accept( MediaType.APPLICATION_JSON ).put( Entity.json( lJsonVendor ) );
      iVendorResponseTypeString = iResponse.readEntity( String.class );

      // Convert to JSON for parsing
      StringReader lReader = new StringReader( iVendorResponseTypeString );
      JsonReader lJsonReader = Json.createReader( lReader );
      iJsonResponse = lJsonReader.readObject();
      lJsonReader.close();
   }


   @Then( "^the vendor is unapproved with the provided information$" )
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

      String lVendorCode = iJsonResponse.getString( CODE );
      String lVendorName = iJsonResponse.getString( VENDOR_NAME );
      String lOrganizationCode = iJsonResponse.getString( ORGANIZATION_CODE );
      String lCertExpDate = iJsonResponse.getString( CERT_EXP_DATE );
      String lVendorType = iJsonResponse.getString( VENDOR_TYPE_CODE );
      String lVendorCurrency = iJsonResponse.getString( CURRENCY_CODE );
      String lMinPurchaseAmount = iJsonResponse.getString( MIN_PURCHASE_AMOUNT );
      String lVendorTimeZone = iJsonResponse.getString( TIME_ZONE );
      String lExtKey = iJsonResponse.getString( EXTERNAL_KEY );

      JsonObject lApprovalList = iJsonResponse.getJsonArray( "vendorOrgApprovals" )
            .getJsonObject( 0 ).getJsonArray( "approvalList" ).getJsonObject( 0 );

      String lReasonCode = lApprovalList.getString( REASON_CODE );
      String lStatus = lApprovalList.getString( STATUS );
      String lNote = lApprovalList.getString( NOTE );

      Assert.assertEquals( aTableRow.get( CODE ), lVendorCode );
      Assert.assertEquals( aTableRow.get( VENDOR_NAME ), lVendorName );
      Assert.assertEquals( aTableRow.get( ORGANIZATION_CODE ), lOrganizationCode );
      Assert.assertEquals( aTableRow.get( CERT_EXP_DATE ), lCertExpDate );
      Assert.assertEquals( aTableRow.get( VENDOR_TYPE_CODE ), lVendorType );
      Assert.assertEquals( aTableRow.get( CURRENCY_CODE ), lVendorCurrency );
      Assert.assertEquals( aTableRow.get( MIN_PURCHASE_AMOUNT ), lMinPurchaseAmount );
      Assert.assertEquals( aTableRow.get( TIME_ZONE ), lVendorTimeZone );
      Assert.assertEquals( aTableRow.get( EXTERNAL_KEY ), lExtKey );
      Assert.assertEquals( aTableRow.get( REASON_CODE ), lReasonCode );
      Assert.assertEquals( aTableRow.get( STATUS ), lStatus );
      Assert.assertEquals( aTableRow.get( NOTE ), lNote );

   }
}
