package com.mxi.am.api.resource.finance;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.mxi.am.api.helper.Credentials;
import com.mxi.am.api.resource.ObjectMapperFactory;

import io.restassured.RestAssured;
import io.restassured.response.Response;


/**
 * Finance Account API test
 *
 */

public class FinanceAccountResourceBeanTest {

   private static final String APPLICATION_JSON = "application/json";
   private static final String FINANCE_ACCOUNT_PATH = "/amapi/" + FinanceAccount.PATH;

   private static final String ACCOUNT_TYPE_CD = "EXPENSE";
   private static final String EXT_KEY_SDESC = "00000000000000000000000000000003";
   private static final boolean CLOSED_BOOL = true;
   private static final String INVALID_FINANCE_ACCOUNT_ALT_ID = "00000000000000000000000000000123";

   private FinanceAccount financeAccount;
   private String financeAccountAltId;


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
   public void setUpData()
         throws JsonParseException, JsonMappingException, IOException, ParseException {

      prepareData();
   }


   @Test
   public void testCreateFinanceAccountSuccessReturns200()
         throws JsonProcessingException, IOException, ParseException {

      String randomAccount =
            "EXPENSE-2-" + StringUtils.upperCase( RandomStringUtils.random( 10, true, false ) );
      financeAccount.setCode( randomAccount );
      financeAccount.setName( randomAccount );
      Response response = createFinanceAccount( 200, Credentials.AUTHENTICATED, financeAccount );
      assertFinanceAccountForCreate( financeAccount, response );
   }


   @Test
   public void testCreateFinanceAccountUnauthenticatedReturns401() throws ParseException {
      createFinanceAccount( 401, Credentials.UNAUTHENTICATED, financeAccount );
   }


   @Test
   public void testCreateFinanceAccountUnauthorizedAccessReturns403() {
      createFinanceAccount( 403, Credentials.UNAUTHORIZED, financeAccount );
   }


   @Test
   public void testGetFinanceAccountByIdSuccessReturns200()
         throws JsonProcessingException, IOException {
      financeAccount.setAltId( financeAccountAltId );
      Response response = getFinanceAccount( 200, Credentials.AUTHENTICATED, financeAccountAltId );
      assertFinanceAccount( financeAccount, response );
   }


   @Test
   public void testGetFinanceAccountByIdNotFoundReturns404() {
      getFinanceAccount( 404, Credentials.AUTHENTICATED, INVALID_FINANCE_ACCOUNT_ALT_ID );
   }


   @Test
   public void testGetFinanceAccountByIdUnauthenticatedReturns401()
         throws JsonParseException, JsonMappingException, IOException {
      getFinanceAccount( 401, Credentials.UNAUTHENTICATED, financeAccountAltId );
   }


   @Test
   public void testGetFinanceAccountByIdUnauthorizedAccessReturns403() {
      getFinanceAccount( 403, Credentials.UNAUTHORIZED, financeAccountAltId );
   }


   @Test
   public void testSearchFinanceAccountSuccessReturns200()
         throws JsonParseException, JsonMappingException, IOException {

      Response response = searchFinanceAccount( 200, Credentials.AUTHORIZED );
      List<FinanceAccount> financeAccounts =
            response.jsonPath().getList( "", FinanceAccount.class );

      financeAccount.setAltId( financeAccountAltId );
      Assert.assertTrue( "The Finance Account with account code '" + financeAccount.getCode()
            + "' was not in the response", financeAccounts.contains( financeAccount ) );
   }


   @Test
   public void testSearchFinanceAccountUnauthenticatedReturns401()
         throws JsonParseException, JsonMappingException, IOException {
      searchFinanceAccount( 401, Credentials.UNAUTHENTICATED );
   }


   @Test
   public void testSearchFinanceAccountUnauthorizedAccessReturns403() {
      searchFinanceAccount( 403, Credentials.UNAUTHORIZED );
   }


   @Test
   public void testUpdateFinanceAccountSuccessReturns200()
         throws JsonProcessingException, IOException {

      financeAccount.setAltId( financeAccountAltId );
      financeAccount.setClosed( CLOSED_BOOL );
      Response response = updateFinanceAccount( 200, Credentials.AUTHENTICATED, financeAccountAltId,
            financeAccount );
      assertFinanceAccount( financeAccount, response );
   }


   @Test
   public void testUpdateFinanceAccountWithInvalidIdReturns404() {
      financeAccount.setClosed( CLOSED_BOOL );
      updateFinanceAccount( 404, Credentials.AUTHENTICATED, INVALID_FINANCE_ACCOUNT_ALT_ID,
            financeAccount );
   }


   @Test
   public void testUpdateFinanceAccountUnauthenticatedReturns401() {
      financeAccount.setClosed( CLOSED_BOOL );
      updateFinanceAccount( 401, Credentials.UNAUTHENTICATED, financeAccountAltId, financeAccount );
   }


   @Test
   public void testUpdateFinanceAccountUnauthorizedAccessReturns403() {
      financeAccount.setClosed( CLOSED_BOOL );
      updateFinanceAccount( 403, Credentials.UNAUTHORIZED, financeAccountAltId, financeAccount );
   }


   private Response createFinanceAccount( int statusCode, Credentials security,
         Object financeAccount ) {
      String username = security.getUserName();
      String password = security.getPassword();

      Response response = RestAssured.given().auth().preemptive().basic( username, password )
            .accept( APPLICATION_JSON ).contentType( APPLICATION_JSON ).body( financeAccount )
            .expect().statusCode( statusCode ).when().post( FINANCE_ACCOUNT_PATH );
      return response;
   }


   private Response getFinanceAccount( int statusCode, Credentials security,
         String financeAccountId ) {
      String username = security.getUserName();
      String password = security.getPassword();

      Response response = RestAssured.given().auth().preemptive().basic( username, password )
            .accept( APPLICATION_JSON ).contentType( APPLICATION_JSON ).expect()
            .statusCode( statusCode ).when().get( FINANCE_ACCOUNT_PATH + "/" + financeAccountId );
      return response;
   }


   private Response updateFinanceAccount( int statusCode, Credentials security,
         String financeAccountId, FinanceAccount financeAccount ) {

      String username = security.getUserName();
      String password = security.getPassword();

      Response response = RestAssured.given().auth().preemptive().basic( username, password )
            .accept( APPLICATION_JSON ).contentType( APPLICATION_JSON ).body( financeAccount )
            .expect().statusCode( statusCode ).when()
            .put( FINANCE_ACCOUNT_PATH + "/" + financeAccountId );
      return response;
   }


   private Response searchFinanceAccount( int statusCode, Credentials security ) {
      String username = security.getUserName();
      String password = security.getPassword();

      Response response = RestAssured.given()
            .queryParam( FinanceAccountSearchParameters.PARAM_ACCOUNT_CODE,
                  financeAccount.getCode() )
            .auth().preemptive().basic( username, password ).accept( APPLICATION_JSON )
            .contentType( APPLICATION_JSON ).expect().statusCode( statusCode ).when()
            .get( FINANCE_ACCOUNT_PATH );
      return response;
   }


   private FinanceAccount defaultFinanceAccountBuilder() throws ParseException {

      String randomAccount =
            "EXPENSE-1-" + StringUtils.upperCase( RandomStringUtils.random( 10, true, false ) );

      FinanceAccount financeAccount = new FinanceAccount();
      financeAccount.setCode( randomAccount );
      financeAccount.setAccountTypeCd( ACCOUNT_TYPE_CD );
      financeAccount.setName( randomAccount );
      financeAccount.setExternalId( EXT_KEY_SDESC );

      return financeAccount;
   }


   private void assertFinanceAccountForCreate( FinanceAccount financeAccountExpected,
         Response actual ) throws JsonProcessingException, IOException {

      FinanceAccount financeAccountActual = ObjectMapperFactory.DEFAULT_OBJECT_MAPPER_FACTORY
            .createObjectMapper().readValue( actual.getBody().asString(), FinanceAccount.class );

      financeAccountExpected.setAltId( financeAccountActual.getAltId() );

      Assert.assertEquals( financeAccountExpected, financeAccountActual );
   }


   private void assertFinanceAccount( FinanceAccount financeAccountExpected, Response actual )
         throws JsonProcessingException, IOException {

      FinanceAccount financeAccountActual = ObjectMapperFactory.DEFAULT_OBJECT_MAPPER_FACTORY
            .createObjectMapper().readValue( actual.getBody().asString(), FinanceAccount.class );

      Assert.assertEquals( financeAccountExpected, financeAccountActual );
   }


   private void prepareData()
         throws JsonParseException, JsonMappingException, IOException, ParseException {
      financeAccount = defaultFinanceAccountBuilder();

      Response searchResponse = searchFinanceAccount( 200, Credentials.AUTHORIZED );
      List<FinanceAccount> financeAccounts =
            searchResponse.jsonPath().getList( "", FinanceAccount.class );

      if ( !financeAccounts.isEmpty() ) {
         financeAccountAltId = financeAccounts.get( 0 ).getAltId();
      }

      else {
         Response CreateResponse =
               createFinanceAccount( 200, Credentials.AUTHENTICATED, financeAccount );

         FinanceAccount financeAccountActual =
               ObjectMapperFactory.DEFAULT_OBJECT_MAPPER_FACTORY.createObjectMapper()
                     .readValue( CreateResponse.getBody().asString(), FinanceAccount.class );

         financeAccountAltId = financeAccountActual.getAltId();
      }
   }
}
