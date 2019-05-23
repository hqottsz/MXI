package com.mxi.am.api.resource.sys.refterm.currency;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.mxi.am.api.helper.Credentials;
import com.mxi.am.api.resource.ObjectMapperFactory;

import io.restassured.RestAssured;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.mapper.factory.Jackson2ObjectMapperFactory;
import io.restassured.response.Response;


/**
 * Currency API Tests
 *
 */
public class CurrencyResourceBeanTest {

   private static final String APPLICATION_JSON = "application/json";
   private static final String CURRENCY_PATH = "/amapi/" + Currency.PATH;

   // Currency Data
   private static final String CURRENCY_CD = "USD";
   private static final String CURRENCY_SDESC = "US Dollars";
   private static final Integer SUB_UNITS_QTY = 2;
   private static final BigDecimal EXCHG_RATE = BigDecimal.ONE;


   @BeforeClass
   public static void setUpClass() {
      try {
         JacksonJsonProvider lJsonProvider = new JacksonJsonProvider();

         final ObjectMapper sObjectMapper =
               ObjectMapperFactory.DEFAULT_OBJECT_MAPPER_FACTORY.createObjectMapper();
         lJsonProvider.setMapper( sObjectMapper );

         RestAssured.reset();
         RestAssured.config =
               RestAssuredConfig.config().objectMapperConfig( new ObjectMapperConfig()
                     .jackson2ObjectMapperFactory( new Jackson2ObjectMapperFactory() {

                        @Override
                        public ObjectMapper create( @SuppressWarnings( "rawtypes" ) Class aClass,
                              String s ) {
                           return sObjectMapper;
                        }
                     } ) );
         RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
         RestAssured.baseURI = "http://".concat( InetAddress.getLocalHost().getHostName() );

         RestAssured.port = 80;

      } catch ( UnknownHostException e ) {
         throw new RuntimeException( "Cannot find local host name." );
      }

   }


   @Test
   public void testGetCurrencyByCurrencyCodeSuccessReturns200()
         throws JsonProcessingException, IOException {
      Currency lCurrency = defaultCurrency();
      Response lResponse = getByCurrencyCd( 200, Credentials.AUTHORIZED, CURRENCY_CD );
      assertCurrency( lCurrency, lResponse );

   }


   @Test
   public void testGetCurrencyByCurrencyCodeNotFoundReturn404()
         throws JsonProcessingException, IOException {
      String lInvalidCurrencyCd = "ICC";
      getByCurrencyCd( 404, Credentials.AUTHORIZED, lInvalidCurrencyCd );

   }


   /**
    * Test get method for unauthorized access
    *
    */
   @Test
   public void testGetCurrencyByCurrencyCodeUnauthorizedReturns403() {
      Currency lCurrency = defaultCurrency();
      getByCurrencyCd( 403, Credentials.UNAUTHORIZED, CURRENCY_CD );
   }


   private Response getByCurrencyCd( int aStatusCode, Credentials aSecurity, String aCurrencyCd ) {
      return getByCurrencyCd( aStatusCode, aSecurity, aCurrencyCd, APPLICATION_JSON,
            APPLICATION_JSON );
   }


   private Response getByCurrencyCd( int aStatusCode, Credentials aSecurity, String aCurrencyCd,
         String aContentType, String aAcceptType ) {
      String lUserName = aSecurity.getUserName();
      String lPassword = aSecurity.getPassword();

      Response lResponse = RestAssured.given().auth().preemptive().basic( lUserName, lPassword )
            .accept( aAcceptType ).contentType( aContentType ).expect().statusCode( aStatusCode )
            .when().get( CURRENCY_PATH + "/" + aCurrencyCd );
      return lResponse;
   }


   private void assertCurrency( Currency aExpectedCurrency, Response aActualCurrency )
         throws JsonProcessingException, IOException {

      Currency lActualCurrency = ObjectMapperFactory.DEFAULT_OBJECT_MAPPER_FACTORY
            .createObjectMapper().readValue( aActualCurrency.getBody().asString(), Currency.class );

      Assert.assertEquals( aExpectedCurrency.getCode(), lActualCurrency.getCode() );
      Assert.assertEquals( aExpectedCurrency.getName(), lActualCurrency.getName() );
      Assert.assertEquals( aExpectedCurrency.getExchangeRate(), lActualCurrency.getExchangeRate() );
      Assert.assertEquals( aExpectedCurrency.getSubUnitsQuantity(),
            lActualCurrency.getSubUnitsQuantity() );

   }


   private Currency defaultCurrency() {
      Currency lCurrency = new Currency();
      lCurrency.setCode( CURRENCY_CD );
      lCurrency.setName( CURRENCY_SDESC );
      lCurrency.setExchangeRate( EXCHG_RATE );
      lCurrency.setSubUnitsQuantity( SUB_UNITS_QTY );
      return lCurrency;
   }

}
