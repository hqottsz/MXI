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
import com.mxi.am.driver.integrationtesting.Rest;
import com.mxi.am.driver.integrationtesting.RestDriver;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;


/**
 * Step definitions for the create vendor feature.
 *
 */
public class CreateVendorAPIStepDefinitions {

   /* Authentication Constants */
   private static final String AMAPI = "/amapi/";

   /* Inbound Create Vendor Constants */
   private static final String LOCATION_ID = "190C9FB7AA9711E687DFE501A36586E0";
   private static final String CODE = "code";
   private static final String VENDOR_NAME = "vendorName";
   private static final String ORGANIZATION_CODE = "organizationCode";
   private static final String CERT_EXP_DATE = "certificateExpiryDate";
   private static final String VENDOR_TYPE_CODE = "vendorTypeCode";
   private static final String CURRENCY_CODE = "currencyCode";
   private static final String MIN_PURCHASE_AMOUNT = "minPurchaseAmount";
   private static final String EXTERNAL_KEY = "externalKey";
   private static final String TIME_ZONE = "timeZone";

   private static final String ADDRESS1 = "address1";
   private static final String CITY = "city";
   private static final String COUNTRY = "country";
   private static final String NAME = "name";

   /* Message */
   private static final String MESSAGE = "message";

   @Inject
   @Rest
   private RestDriver iRestDriver;

   public static Vendor iVendor;
   public static Response iResponse;
   public static JsonObject iJsonResponse;
   public static String iVendorResponseTypeString;


   @When( "^a Create Vendor API request is sent to Maintenix$" )
   public void createVendorWithVendorCode( List<Map<String, String>> aDataTable ) throws Throwable {

      Map<String, String> aTableRow = aDataTable.get( 0 );

      iVendor = new Vendor();

      SimpleDateFormat lDateFormatter = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ss.SSSZ" );
      Date lExpireDate = lDateFormatter.parse( aTableRow.get( CERT_EXP_DATE ) );

      iVendor.setLocationId( LOCATION_ID );
      iVendor.setCode( aTableRow.get( CODE ) );
      iVendor.setVendorName( aTableRow.get( VENDOR_NAME ) );
      iVendor.setOrganizationCode( aTableRow.get( ORGANIZATION_CODE ) );
      iVendor.setCertificateExpiryDate( lExpireDate );
      iVendor.setVendorTypeCode( aTableRow.get( VENDOR_TYPE_CODE ) );
      iVendor.setCurrencyCode( aTableRow.get( CURRENCY_CODE ) );
      iVendor.setMinPurchaseAmount( aTableRow.get( MIN_PURCHASE_AMOUNT ) );
      iVendor.setTimeZone( aTableRow.get( TIME_ZONE ) );
      iVendor.setExternalKey( aTableRow.get( EXTERNAL_KEY ) );

      iVendor.setShippingAddress( new ShippingAddress() );
      iVendor.getShippingAddress().setAddress1( aTableRow.get( ADDRESS1 ) );
      iVendor.getShippingAddress().setCity( aTableRow.get( CITY ) );
      iVendor.getShippingAddress().setCountry( aTableRow.get( COUNTRY ) );

      List<Contact> lListOfContacts = new ArrayList<Contact>();
      Contact lContact = new Contact();
      lContact.setName( aTableRow.get( NAME ) );
      lContact.setMainContact( true );
      lListOfContacts.add( lContact );

      iVendor.setContacts( lListOfContacts );

      ObjectMapper lMapper = ObjectMapperFactory.DEFAULT_OBJECT_MAPPER_FACTORY.createObjectMapper();
      String lJsonVendor = lMapper.writerWithDefaultPrettyPrinter().writeValueAsString( iVendor );

      // We want response as JSON hence MediaType.APPLICATION_JSON
      iResponse = iRestDriver.target( AMAPI + Vendor.PATH ).request()
            .accept( MediaType.APPLICATION_JSON ).post( Entity.json( lJsonVendor ) );
      iVendorResponseTypeString = iResponse.readEntity( String.class );

      // Convert to JSON for parsing
      StringReader lReader = new StringReader( iVendorResponseTypeString );
      JsonReader lJsonReader = Json.createReader( lReader );
      iJsonResponse = lJsonReader.readObject();
      lJsonReader.close();
   }


   @Then( "^the vendor is created with the provided information$" )
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

      Assert.assertEquals( aTableRow.get( CODE ), lVendorCode );
      Assert.assertEquals( aTableRow.get( VENDOR_NAME ), lVendorName );
      Assert.assertEquals( aTableRow.get( ORGANIZATION_CODE ), lOrganizationCode );
      Assert.assertEquals( aTableRow.get( CERT_EXP_DATE ), lCertExpDate );
      Assert.assertEquals( aTableRow.get( VENDOR_TYPE_CODE ), lVendorType );
      Assert.assertEquals( aTableRow.get( CURRENCY_CODE ), lVendorCurrency );
      Assert.assertEquals( aTableRow.get( MIN_PURCHASE_AMOUNT ), lMinPurchaseAmount );
      Assert.assertEquals( aTableRow.get( TIME_ZONE ), lVendorTimeZone );
      Assert.assertEquals( aTableRow.get( EXTERNAL_KEY ), lExtKey );

   }


   @Then( "^the error message \"([^\"]*)\" is verified$" )
   public void aResponseWithTheErrorMessageIsReceived( String aErrorMessage ) throws Throwable {
      String lErrorMessage = iJsonResponse.getString( MESSAGE );
      Assert.assertTrue(
            "The expected error was: " + aErrorMessage + " The actual error was: " + lErrorMessage,
            lErrorMessage.contains( aErrorMessage ) );
   }
}
