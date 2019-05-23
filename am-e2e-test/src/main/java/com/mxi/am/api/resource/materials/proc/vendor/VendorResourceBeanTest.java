package com.mxi.am.api.resource.materials.proc.vendor;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mxi.am.api.helper.Credentials;
import com.mxi.am.api.resource.ObjectMapperFactory;
import com.mxi.am.api.resource.materials.proc.vendor.Vendor.Contact;
import com.mxi.am.api.resource.materials.proc.vendor.Vendor.ShippingAddress;

import io.restassured.RestAssured;
import io.restassured.response.Response;


/**
 * Vendor API Test
 *
 */
public class VendorResourceBeanTest {

   private static final String TYPE_CODE = "PURCHASE";
   private static final String CURRENCY_CODE = "USD";
   private static final String ORGANIZATION_CODE = "MXI";
   private static final String APPROVAL_CODE = "PURCHASE";
   private static final String TERMS_AND_CONDITION = "NET30";
   private static final String DEFAULT_AIRPORT = "DEFAULT_AIRPORT";
   private static final String ADDRESS_1 = "1431 Blair Pl";
   private static final String CITY = "Ottawa";
   private static final String COUNTRY = "CAN";
   private static final String STATE = "ON";
   private static final String TIME_ZONE = "America/Montreal";
   private static final String VENDOR_NAME = "Vendor MP";
   private static final String CONTACT_NAME = "SPARES1 TEAM";
   private static final String AIRPORT_LOCATION = "AIRPORT1";

   private static final String APPLICATION_JSON = "application/json";
   private static final String VENDOR_PATH = "/amapi/" + Vendor.PATH;
   private static final Random RANDOM = new Random();

   private static Vendor defaultVendor;

   private final ObjectMapper objectMapper =
         ObjectMapperFactory.DEFAULT_OBJECT_MAPPER_FACTORY.createObjectMapper();


   @BeforeClass
   public static void setUpClass() throws JsonParseException, JsonMappingException, IOException {
      try {
         RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
         RestAssured.baseURI = "http://".concat( InetAddress.getLocalHost().getHostName() );

         RestAssured.port = 80;

      } catch ( UnknownHostException e ) {
         throw new RuntimeException( "Cannot find local host name." );
      }

      prepareData();
   }


   private static void prepareData() throws JsonParseException, JsonMappingException, IOException {
      // create a vendor to be used in tests
      Vendor vendor = defaultVendor();
      Response response = create( 200, Credentials.AUTHENTICATED, vendor );
      defaultVendor = ObjectMapperFactory.DEFAULT_OBJECT_MAPPER_FACTORY.createObjectMapper()
            .readValue( response.getBody().asString(), Vendor.class );
   }


   @Test
   public void testGetVendorByIdSuccess()
         throws JsonParseException, JsonMappingException, IOException {
      Response response = getById( 200, Credentials.AUTHORIZED, defaultVendor.getId() );
      Vendor actualVendor = objectMapper.readValue( response.getBody().asString(), Vendor.class );

      assertVendor( defaultVendor, actualVendor );
   }


   @Test
   public void testGetVendorNotFoundById()
         throws JsonParseException, JsonMappingException, IOException {
      Response response = getById( 404, Credentials.AUTHORIZED, "INVALID" );
      Assert.assertEquals( 404, response.getStatusCode() );
   }


   @Test
   public void testGetVendorByIDUnauthorizedReturns403() throws IOException {
      getById( 403, Credentials.UNAUTHORIZED, defaultVendor.getId() );
   }


   @Test
   public void testGetVendorByIDUnauthenticatedReturns401() throws IOException {
      getById( 401, Credentials.UNAUTHENTICATED, defaultVendor.getId() );
   }


   @Test
   public void testSearchVendorByCodeSuccess()
         throws JsonParseException, JsonMappingException, IOException {
      Response response = SearchByCode( 200, Credentials.AUTHORIZED, defaultVendor.getCode() );
      List<Vendor> actualVendor = objectMapper.readValue( response.getBody().asString(),
            objectMapper.getTypeFactory().constructCollectionType( List.class, Vendor.class ) );
      assertVendor( defaultVendor, actualVendor.get( 0 ) );
   }


   @Test
   public void testSearchVendorByCodeUnauthenticatedReturns401() {
      SearchByCode( 401, Credentials.UNAUTHENTICATED, defaultVendor.getCode() );
   }


   @Test
   public void testSearchVendorByCodeUnauthorizedReturns403() {
      SearchByCode( 403, Credentials.UNAUTHORIZED, defaultVendor.getCode() );
   }


   @Test
   public void testCreateVendorSuccess()
         throws JsonParseException, JsonMappingException, IOException {
      Vendor vendor = defaultVendor();
      String code = String.valueOf( Calendar.getInstance().getTimeInMillis() );
      vendor.setCode( String.valueOf( code.substring( code.length() - 5, code.length() ) ) );
      Response response = create( 200, Credentials.AUTHENTICATED, vendor );
      Vendor actualVendor = objectMapper.readValue( response.getBody().asString(), Vendor.class );

      assertVendor( vendor, actualVendor );
   }


   @Test
   public void testCreateVendorWithInvalidAuthentication401()
         throws JsonParseException, JsonMappingException, IOException {
      Vendor newVendor = new Vendor();
      create( 401, Credentials.UNAUTHENTICATED, newVendor );
   }


   @Test
   public void testCreateVendorWithInvalidAuthorization403()
         throws JsonParseException, JsonMappingException, IOException {
      Vendor newVendor = new Vendor();
      create( 403, Credentials.UNAUTHORIZED, newVendor );
   }


   @Test
   public void testUpdateVendorSuccess()
         throws JsonParseException, JsonMappingException, IOException {
      Response response = getById( 200, Credentials.AUTHORIZED, defaultVendor.getId() );
      Vendor newVendor = objectMapper.readValue( response.getBody().asString(), Vendor.class );

      List<String> spec2000Commands = new ArrayList<String>();
      spec2000Commands.add( "S1SHIPPD" );
      newVendor.setSpec2000Commands( spec2000Commands );

      response = updateVendor( 200, Credentials.AUTHENTICATED, newVendor.getId(), newVendor );

      Vendor updatedVendor = objectMapper.readValue( response.getBody().asString(), Vendor.class );

      assertVendor( newVendor, updatedVendor );

   }


   @Test
   public void testUpdateVendorWithInvalidAuthentication401()
         throws JsonParseException, JsonMappingException, IOException {
      Vendor updateDetails = new Vendor();
      updateVendor( 401, Credentials.UNAUTHENTICATED, defaultVendor.getId(), updateDetails );

   }


   @Test
   public void testUpdateVendorWithInvalidAuthorization403()
         throws JsonParseException, JsonMappingException, IOException {
      Vendor updateDetails = new Vendor();
      updateVendor( 403, Credentials.UNAUTHORIZED, defaultVendor.getId(), updateDetails );

   }


   private Response updateVendor( int statusCode, Credentials security, String vendorId,
         Vendor aVendor ) {
      String userName = security.getUserName();
      String password = security.getPassword();

      Response response = RestAssured.given().auth().preemptive().basic( userName, password )
            .accept( APPLICATION_JSON ).contentType( APPLICATION_JSON ).body( aVendor ).expect()
            .statusCode( statusCode ).when().put( VENDOR_PATH + "/" + vendorId );

      return response;
   }


   private static Response create( int statusCode, Credentials security, Object vendor ) {
      String userName = security.getUserName();
      String password = security.getPassword();

      Response response = RestAssured.given().auth().preemptive().basic( userName, password )
            .accept( APPLICATION_JSON ).contentType( APPLICATION_JSON ).body( vendor ).expect()
            .statusCode( statusCode ).when().post( VENDOR_PATH );
      return response;
   }


   /**
    * Searches a Vendor by code using the vendor API
    *
    * @param aI
    * @param authorized
    * @param vendorCd
    * @return
    */
   private Response SearchByCode( int statusCode, Credentials security, String vendorCd ) {
      String username = security.getUserName();
      String password = security.getPassword();

      Response lResponse = RestAssured.given().auth().preemptive().basic( username, password )
            .accept( APPLICATION_JSON ).contentType( APPLICATION_JSON ).expect()
            .statusCode( statusCode ).when().get( VENDOR_PATH + "/?code=" + vendorCd );
      return lResponse;
   }


   /**
    * Gets a vendor by ID using the vendor API
    *
    * @param aI
    * @param authorized
    * @param vendorId
    * @return
    */
   private static Response getById( int statusCode, Credentials security, String vendorId ) {
      String username = security.getUserName();
      String password = security.getPassword();

      Response response = RestAssured.given().auth().preemptive().basic( username, password )
            .accept( APPLICATION_JSON ).contentType( APPLICATION_JSON ).expect()
            .statusCode( statusCode ).when().get( VENDOR_PATH + "/" + vendorId );
      return response;
   }


   /**
    * Checks the equality of 2 vendors
    *
    * @param vendor
    * @param response
    * @throws IOException
    * @throws JsonMappingException
    * @throws JsonParseException
    */
   private void assertVendor( Vendor expectedVendor, Vendor actualVendor )
         throws JsonParseException, JsonMappingException, IOException {

      Assert.assertEquals( expectedVendor.getCode(), actualVendor.getCode() );
      Assert.assertEquals( expectedVendor.getVendorTypeCode(), actualVendor.getVendorTypeCode() );
      Assert.assertEquals( expectedVendor.getCurrencyCode(), actualVendor.getCurrencyCode() );
      Assert.assertEquals( expectedVendor.getOrganizationCode(),
            actualVendor.getOrganizationCode() );

      Assert.assertEquals( expectedVendor.getApprovalTypeCode(),
            actualVendor.getApprovalTypeCode() );
      Assert.assertEquals( expectedVendor.getTermsAndConds(), actualVendor.getTermsAndConds() );
      Assert.assertEquals( expectedVendor.isSpec2000Enabled(), actualVendor.isSpec2000Enabled() );

      Assert.assertEquals( expectedVendor.getShippingAddress().getAddress1(),
            actualVendor.getShippingAddress().getAddress1() );

      Assert.assertEquals( expectedVendor.getShippingAddress().getCity(),
            actualVendor.getShippingAddress().getCity() );
      Assert.assertEquals( expectedVendor.getShippingAddress().getCountry(),
            actualVendor.getShippingAddress().getCountry() );
      Assert.assertEquals( expectedVendor.getShippingAddress().getState(),
            actualVendor.getShippingAddress().getState() );

      Assert.assertEquals( expectedVendor.getContacts().get( 0 ).getName(),
            actualVendor.getContacts().get( 0 ).getName() );
      Assert.assertEquals( expectedVendor.getContacts().get( 0 ).isMainContact(),
            actualVendor.getContacts().get( 0 ).isMainContact() );
      Assert.assertEquals( expectedVendor.getTimeZone(), actualVendor.getTimeZone() );
      Assert.assertEquals( expectedVendor.getVendorName(), actualVendor.getVendorName() );
   }


   /**
    * creates a default vendor object
    *
    * @return
    */
   private static Vendor defaultVendor() {
      Vendor vendor = new Vendor();
      vendor.setCode( generateVendorCode() );
      vendor.setVendorTypeCode( TYPE_CODE );
      vendor.setCurrencyCode( CURRENCY_CODE );
      vendor.setOrganizationCode( ORGANIZATION_CODE );
      vendor.setApprovalTypeCode( APPROVAL_CODE );
      vendor.setTermsAndConds( TERMS_AND_CONDITION );
      vendor.setSpec2000Enabled( true );
      vendor.setDefaultAirport( DEFAULT_AIRPORT );
      ShippingAddress shippinAddress = new ShippingAddress();
      shippinAddress.setAddress1( ADDRESS_1 );
      shippinAddress.setCity( CITY );
      shippinAddress.setCountry( COUNTRY );
      shippinAddress.setState( STATE );
      vendor.setShippingAddress( shippinAddress );
      vendor.setTimeZone( TIME_ZONE );
      vendor.setCertificateExpiryDate( new Date( 100000 ) );
      vendor.setMinPurchaseAmount( "100" );
      vendor.setDefaultAirport( AIRPORT_LOCATION );

      vendor.setVendorName( VENDOR_NAME );

      List<Contact> contacts = new ArrayList<Contact>();
      Contact contact = new Contact();
      contact.setName( CONTACT_NAME );
      contact.setMainContact( true );
      contacts.add( contact );
      vendor.setContacts( contacts );
      return vendor;
   }


   private static String generateVendorCode() {
      return "V" + RANDOM.nextInt( 999999999 );
   }

}
