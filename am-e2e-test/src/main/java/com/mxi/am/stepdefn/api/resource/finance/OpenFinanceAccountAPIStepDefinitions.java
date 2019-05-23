package com.mxi.am.stepdefn.api.resource.finance;

import java.io.StringReader;
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
import com.mxi.am.api.resource.finance.FinanceAccount;
import com.mxi.am.driver.integrationtesting.Rest;
import com.mxi.am.driver.integrationtesting.RestDriver;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;


/**
 * Open Finance Account API Step Definitions
 *
 */
public class OpenFinanceAccountAPIStepDefinitions {

   /* Authentication Constants */
   private static final String USERNAME = "mxintegration";
   private static final String PASSWORD = "password";
   private static final String AMAPI = "/amapi/";

   /* Variables */
   private static final String NAME = "name";
   private static final String CODE = "code";
   private static final String ACCOUNT_TYPE_CD = "accountTypeCd";
   private static final String EXTERNAL_ID = "externalId";
   private static final String CLOSED = "closed";

   private static final String ALT_ID = "altId";

   /* Message */
   private static final String MESSAGE = "message";

   @Inject
   @Rest
   private RestDriver iRestDriver;

   public static FinanceAccount iFinanceAccount;
   public static JsonObject iJsonResponse;
   public static Response iResponse;
   public static String iAltId;


   @Given( "^a Create Finance Account API request is sent to Maintenix$" )
   public void thatAFinanceAccountIsCreatedInTheClosedState( List<Map<String, String>> aDataTable )
         throws Throwable {
      Map<String, String> aTableRow = aDataTable.get( 0 );

      iFinanceAccount = new FinanceAccount();

      iFinanceAccount.setName( aTableRow.get( NAME ) );
      iFinanceAccount.setCode( aTableRow.get( CODE ) );
      iFinanceAccount.setAccountTypeCd( aTableRow.get( ACCOUNT_TYPE_CD ) );
      iFinanceAccount.setExternalId( aTableRow.get( EXTERNAL_ID ) );
      iFinanceAccount.setClosed( new Boolean( aTableRow.get( CLOSED ) ) );

      ObjectMapper lMapper = ObjectMapperFactory.DEFAULT_OBJECT_MAPPER_FACTORY.createObjectMapper();
      String lJsonAccount =
            lMapper.writerWithDefaultPrettyPrinter().writeValueAsString( iFinanceAccount );

      iResponse = iRestDriver.target( AMAPI + FinanceAccount.PATH ).request()
            .accept( MediaType.APPLICATION_JSON ).post( Entity.json( lJsonAccount ) );

   }


   @When( "^an Update Finance Account API request is sent to Maintenix to open the finance account$" )
   public void anOpenFinanceAccountInboundMessageIsSentToMaintenix(
         List<Map<String, String>> aDataTable ) throws Throwable {
      Map<String, String> aTableRow = aDataTable.get( 0 );

      // We want response as JSON hence MediaType.APPLICATION_JSON
      Response lAccountResponse = iRestDriver
            .target( AMAPI + FinanceAccount.PATH + "?account_code=" + aTableRow.get( CODE ) )
            .request().accept( MediaType.APPLICATION_JSON ).get();

      String lAccountResponseTypeString = lAccountResponse.readEntity( String.class );

      // Convert to JSON for parsing
      StringReader lFinanceAccountReader = new StringReader( lAccountResponseTypeString );
      JsonReader lFinanceAccountJsonReader = Json.createReader( lFinanceAccountReader );
      if ( lAccountResponseTypeString != null ) {
         iJsonResponse = lFinanceAccountJsonReader.readArray().getJsonObject( 0 );
         lFinanceAccountJsonReader.close();
         iAltId = iJsonResponse.getString( ALT_ID );
      }
      iFinanceAccount = new FinanceAccount();

      iFinanceAccount.setName( aTableRow.get( NAME ) );
      iFinanceAccount.setCode( aTableRow.get( CODE ) );
      iFinanceAccount.setClosed( new Boolean( aTableRow.get( CLOSED ) ) );
      iFinanceAccount.setAltId( iAltId );

      ObjectMapper lMapper = ObjectMapperFactory.DEFAULT_OBJECT_MAPPER_FACTORY.createObjectMapper();
      String lJsonAccount =
            lMapper.writerWithDefaultPrettyPrinter().writeValueAsString( iFinanceAccount );
      // We want response as JSON hence MediaType.APPLICATION_JSON
      iResponse = iRestDriver.target( AMAPI + FinanceAccount.PATH + "/" + iAltId ).request()
            .accept( MediaType.APPLICATION_JSON ).put( Entity.json( lJsonAccount ) );
   }


   @Then( "^the finance account is open$" )
   public void theVendorIsCreatedInDbWithTheProvidedInformation(
         List<Map<String, String>> aDataTable ) throws Throwable {

      Map<String, String> aTableRow = aDataTable.get( 0 );

      if ( iResponse.getStatus() != 200 ) {
         String lErrorMessage = iJsonResponse.getString( MESSAGE );
         Assert.fail( "Unexpected http request. Received a " + iResponse.getStatus()
               + " with error message: " + lErrorMessage );
      }

      iResponse = iRestDriver
            .target( AMAPI + FinanceAccount.PATH + "?account_code=" + aTableRow.get( CODE ) )
            .request().accept( MediaType.APPLICATION_JSON ).get();

      String lVendorResponseTypeString = iResponse.readEntity( String.class );

      // Convert to JSON for parsing
      StringReader lReader = new StringReader( lVendorResponseTypeString );
      JsonReader lJsonReader = Json.createReader( lReader );
      iJsonResponse = lJsonReader.readArray().getJsonObject( 0 );
      lJsonReader.close();

      boolean lClosed = iJsonResponse.getBoolean( CLOSED );
      String lName = iJsonResponse.getString( NAME );
      String lCode = iJsonResponse.getString( CODE );
      String lAccountTypeCd = iJsonResponse.getString( ACCOUNT_TYPE_CD );
      String lExternalId = iJsonResponse.getString( EXTERNAL_ID );

      Assert.assertEquals( new Boolean( aTableRow.get( CLOSED ) ), lClosed );
      Assert.assertEquals( aTableRow.get( NAME ), lName );
      Assert.assertEquals( aTableRow.get( CODE ), lCode );
      Assert.assertEquals( aTableRow.get( ACCOUNT_TYPE_CD ), lAccountTypeCd );
      Assert.assertEquals( aTableRow.get( EXTERNAL_ID ), lExternalId );
   }
}
