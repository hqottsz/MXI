package com.mxi.am.api.resource.materials.proc.invoice.historynote;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.RandomStringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mxi.am.api.helper.Credentials;
import com.mxi.am.api.resource.ObjectMapperFactory;
import com.mxi.am.api.resource.materials.proc.invoice.Invoice;
import com.mxi.am.driver.common.database.DatabaseDriver;
import com.mxi.am.driver.common.database.Result;
import com.mxi.am.guice.AssetManagementDatabaseDriverProvider;

import io.restassured.RestAssured;
import io.restassured.response.Response;


/**
 * Invoice History Note API test
 *
 */
public class InvoiceHistoryNoteResourceBeanTest {

   private final String applicationJson = "application/json";
   private final String invoiceHistoryNotePath = "/amapi/" + InvoiceHistoryNote.PATH;
   private static final String INVALID_HISTORYNOTE_ID = "111CE26E465C697777D93C1111EE25A3";

   private DatabaseDriver driver;

   // Invoice History Note Data
   private Invoice invoice;
   private InvoiceHistoryNote invoiceHistoryNote;
   private String invoiceHistoryNoteAltId;
   private String invoiceAltId;
   private String userName = "mxi";
   private String secondUserName = "mxintegration";
   private String userId;
   private String secondUserId;
   private String stausCode = "PIOPEN";
   private String stageReasonCode = "CORRECT";
   private String note = "PO invoice created.";

   private Date date;

   // data for invoice
   private String currencyCode = "USD";
   private BigDecimal exchangeRate = new BigDecimal( "1" );
   private String invoiceStatus = "OPEN";
   private String vendorCode = "50001";
   private String vendorId;
   private final String invoicePath = "/amapi/" + Invoice.PATH;


   @BeforeClass
   public static void setUpClass() {
      try {
         RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
         RestAssured.baseURI = "http://".concat( InetAddress.getLocalHost().getHostName() );

         RestAssured.port = 80;

      } catch ( UnknownHostException e ) {
         throw new RuntimeException( "Cannot find local host name." );
      }

   }


   @Before
   public void setUpData() throws Exception {
      driver = new AssetManagementDatabaseDriverProvider().get();
      Result vendorResult =
            driver.select( "SELECT alt_id FROM org_vendor WHERE vendor_cd = ?", vendorCode );
      if ( vendorResult.isEmpty() ) {
         fail( "Could not find the vendor details of " + vendorCode );
      } else {
         vendorId = vendorResult.get( 0 ).getUuidString( "alt_id" );
      }
      Result userResult =
            driver.select( "SELECT alt_id FROM utl_user WHERE username = ?", userName );
      if ( userResult.isEmpty() ) {
         fail( "Could not find the User details of" + userName );
      } else {
         userId = userResult.get( 0 ).getUuidString( "alt_id" );
      }
      Result secondUserResult =
            driver.select( "SELECT alt_id FROM utl_user WHERE username = ?", secondUserName );
      if ( secondUserResult.isEmpty() ) {
         fail( "Could not find the User details of" + secondUserName );
      } else {
         secondUserId = secondUserResult.get( 0 ).getUuidString( "alt_id" );
      }
   }


   /**
    *
    * Test get method for success
    *
    * @throws JsonProcessingException
    * @throws IOException
    * @throws ParseException
    */
   @Test
   public void testGetHistoryNoteByIdSuccessReturns200()
         throws JsonProcessingException, IOException, ParseException {
      buildInvoice();
      buildInvoiceHistoryNote();

      Response response = getById( 200, Credentials.AUTHENTICATED, invoiceHistoryNoteAltId,
            applicationJson, applicationJson );

      assertHistoryNote( invoiceHistoryNote, response );

   }


   /**
    *
    * Test get method for not found
    */
   @Test
   public void testGetHistoryNoteByIdNotFoundReturns404() {
      getById( 404, Credentials.AUTHENTICATED, INVALID_HISTORYNOTE_ID, applicationJson,
            applicationJson );

   }


   /**
    *
    * Test Search method for success
    *
    * @throws JsonProcessingException
    * @throws IOException
    * @throws ParseException
    */
   @Test
   public void testSearchHistoryNoteByInvoiceIdSuccessReturns200()
         throws JsonProcessingException, IOException, ParseException {
      buildInvoice();
      buildInvoiceHistoryNote();
      Response response = searchById( 200, Credentials.AUTHENTICATED, invoiceAltId, applicationJson,
            applicationJson );
      invoiceHistoryNote.setId( invoiceHistoryNoteAltId );

      assertHistoryNoteSearch( invoiceHistoryNote, response );

   }


   /**
    *
    * Test search method for unauthenticated
    *
    */
   @Test
   public void testSearchHistoryNoteByInvoiceIdUnauthenticatedReturns401() {
      searchById( 401, Credentials.UNAUTHENTICATED, invoiceHistoryNoteAltId, applicationJson,
            applicationJson );

   }


   /**
    *
    * Test create method for success
    *
    * @throws JsonParseException
    * @throws JsonMappingException
    * @throws IOException
    * @throws ParseException
    */
   @Test
   public void testCreateInvoiceHistoryNoteSuccess200()
         throws JsonParseException, JsonMappingException, IOException, ParseException {
      buildInvoice();
      InvoiceHistoryNote InvoiceHistoryNoteCrated = buildInvoiceHistoryNote();
      assertHistoryNoteCreated( invoiceHistoryNote, InvoiceHistoryNoteCrated );
   }


   /**
    * Test create method for InternalSeverError
    */
   @Test
   public void testCreateInvoiceHistoryNoteInternalSeverError500() {
      invoiceHistoryNote = new InvoiceHistoryNote();
      create( 500, Credentials.AUTHENTICATED, invoiceHistoryNote, applicationJson, applicationJson,
            invoiceHistoryNotePath );

   }


   private Response getById( int statusCode, Credentials security, String altId, String contentType,
         String acceptType ) {

      String username = security.getUserName();
      String password = security.getPassword();

      Response response = RestAssured.given().auth().preemptive().basic( username, password )
            .accept( acceptType ).contentType( contentType ).expect().statusCode( statusCode )
            .when().get( invoiceHistoryNotePath + "/" + altId );

      return response;
   }


   private Response searchById( int statusCode, Credentials security, String altId,
         String contentType, String acceptType ) {
      String username = security.getUserName();
      String password = security.getPassword();

      Response response = RestAssured.given()
            .queryParam( InvoiceHistoryNoteSearchParameters.INVOICE_ID, altId ).auth().preemptive()
            .basic( username, password ).accept( acceptType ).contentType( contentType ).expect()
            .statusCode( statusCode ).when().get( invoiceHistoryNotePath );

      return response;
   }


   private Response create( int statusCode, Credentials security, Object object, String contentType,
         String acceptType, String path ) {
      String lUserName = security.getUserName();
      String lPassword = security.getPassword();

      Response lResponse = RestAssured.given().auth().preemptive().basic( lUserName, lPassword )
            .accept( acceptType ).contentType( contentType ).body( object ).expect()
            .statusCode( statusCode ).when().post( path );
      return lResponse;
   }


   public InvoiceHistoryNote buildInvoiceHistoryNote()
         throws JsonParseException, JsonMappingException, IOException, ParseException {
      invoiceHistoryNote = prepareDataForInvoiceHistoryNote();
      Response responseOfInvoiceHistoryNote = create( 200, Credentials.AUTHENTICATED,
            invoiceHistoryNote, applicationJson, applicationJson, invoiceHistoryNotePath );
      InvoiceHistoryNote invoiceHistoryNotecreated =
            ObjectMapperFactory.DEFAULT_OBJECT_MAPPER_FACTORY.createObjectMapper().readValue(
                  responseOfInvoiceHistoryNote.getBody().asString(), InvoiceHistoryNote.class );
      invoiceHistoryNoteAltId = invoiceHistoryNotecreated.getId();
      return invoiceHistoryNotecreated;

   }


   public Invoice buildInvoice()
         throws JsonParseException, JsonMappingException, IOException, ParseException {
      invoice = prepareDataForInvoice();
      Response responseofInvoice = create( 200, Credentials.AUTHENTICATED, invoice, applicationJson,
            applicationJson, invoicePath );

      Invoice invoiceCreated =
            ObjectMapperFactory.DEFAULT_OBJECT_MAPPER_FACTORY.createObjectMapper()
                  .readValue( responseofInvoice.getBody().asString(), Invoice.class );
      invoiceAltId = invoiceCreated.getId();
      return invoiceCreated;
   }


   private InvoiceHistoryNote prepareDataForInvoiceHistoryNote() throws ParseException {
      invoiceHistoryNote = new InvoiceHistoryNote();
      date = getDate();
      invoiceHistoryNote.setStageReasonCode( stageReasonCode );
      invoiceHistoryNote.setInvoiceId( invoiceAltId );
      invoiceHistoryNote.setNote( note );
      invoiceHistoryNote.setDate( date );
      invoiceHistoryNote.setUserId( userId );
      return invoiceHistoryNote;

   }


   private InvoiceHistoryNote prepareDataForInvoiceHistoryNoteTwo() throws ParseException {
      InvoiceHistoryNote invoiceHistoryNoteTwo = new InvoiceHistoryNote();
      invoiceHistoryNoteTwo.setInvoiceId( invoiceAltId );
      invoiceHistoryNoteTwo.setNote( note );
      invoiceHistoryNoteTwo.setUserId( secondUserId );
      invoiceHistoryNoteTwo.setStatus( stausCode );

      return invoiceHistoryNoteTwo;
   }


   private Invoice prepareDataForInvoice() {
      invoice = new Invoice();
      invoice.setCurrencyCode( currencyCode );
      invoice.setInvoiceNumber( RandomStringUtils.random( 20, false, true ) );
      invoice.setInvoiceStatus( invoiceStatus );
      invoice.setExchangeRate( exchangeRate );
      invoice.setVendorId( vendorId );
      return invoice;

   }


   private Date getDate() throws ParseException {

      DateFormat dateFormat = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
      date = new Date();
      String todayDate = dateFormat.format( date );
      date = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" ).parse( todayDate );
      return date;

   }


   private void assertHistoryNote( InvoiceHistoryNote invoiceHistoryNoteExpected,
         Response actualInvoiceHistoryNoteResponse )
         throws JsonParseException, JsonMappingException, IOException {
      InvoiceHistoryNote actualInvoiceHistoryNote =
            ObjectMapperFactory.DEFAULT_OBJECT_MAPPER_FACTORY.createObjectMapper().readValue(
                  actualInvoiceHistoryNoteResponse.getBody().asString(), InvoiceHistoryNote.class );
      invoiceHistoryNoteExpected.setId( invoiceHistoryNoteAltId );
      Assert.assertEquals( invoiceHistoryNoteExpected, actualInvoiceHistoryNote );
   }


   private void assertHistoryNoteSearch( InvoiceHistoryNote invoiceHistoryNoteExpected,
         Response actualInvoiceHistoryNoteResponse )
         throws JsonParseException, JsonMappingException, IOException, ParseException {
      boolean isContains = false;
      ObjectMapper objectMapper =
            ObjectMapperFactory.DEFAULT_OBJECT_MAPPER_FACTORY.createObjectMapper();
      List<InvoiceHistoryNote> ActualInvoiceHistoryNoteList = objectMapper.readValue(
            actualInvoiceHistoryNoteResponse.getBody().asString(), objectMapper.getTypeFactory()
                  .constructCollectionType( List.class, InvoiceHistoryNote.class ) );
      InvoiceHistoryNote invoiceHistoryNoteTwo = prepareDataForInvoiceHistoryNoteTwo();
      for ( InvoiceHistoryNote actualInvoiceHistoryNote : ActualInvoiceHistoryNoteList ) {

         if ( actualInvoiceHistoryNote.getStatus() == null ) {
            isContains = ActualInvoiceHistoryNoteList.contains( invoiceHistoryNoteExpected );
            Assert.assertTrue( isContains );
         } else {
            invoiceHistoryNoteTwo.setId( actualInvoiceHistoryNote.getId() );
            invoiceHistoryNoteTwo.setDate( actualInvoiceHistoryNote.getDate() );
            isContains = ActualInvoiceHistoryNoteList.contains( invoiceHistoryNoteTwo );
            Assert.assertTrue( isContains );
         }
      }

      Assert.assertEquals( 2, ActualInvoiceHistoryNoteList.size() );
   }


   private void assertHistoryNoteCreated( InvoiceHistoryNote invoiceHistoryNoteExpected,
         InvoiceHistoryNote actualinvoiceHistoryNote ) {
      invoiceHistoryNoteExpected.setId( invoiceHistoryNoteAltId );
      Assert.assertEquals( invoiceHistoryNoteExpected, actualinvoiceHistoryNote );
   }

}
