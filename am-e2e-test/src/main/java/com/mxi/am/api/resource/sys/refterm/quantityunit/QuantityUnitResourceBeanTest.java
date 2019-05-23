package com.mxi.am.api.resource.sys.refterm.quantityunit;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mxi.am.api.helper.Credentials;
import com.mxi.am.api.resource.ObjectMapperFactory;

import io.restassured.RestAssured;
import io.restassured.response.Response;


/**
 * Quantity Unit API Tests
 *
 */
public class QuantityUnitResourceBeanTest {

   private static final String APPLICATION_JSON = "application/json";
   private static final String QTY_UNIT_PATH = "/amapi/" + QuantityUnit.PATH;

   // Quantity Unit Data
   private static final String QTY_UNIT_CD = "EA";
   private static final String QTY_UNIT_SDESC = "Each";
   private static final BigDecimal DECIMAL_PLACES_QTY = BigDecimal.ZERO;

   private final ObjectMapper objectMapper =
         ObjectMapperFactory.DEFAULT_OBJECT_MAPPER_FACTORY.createObjectMapper();


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


   @Test
   public void testGetQtyUnitByCodeSuccessReturns200() throws JsonProcessingException, IOException {
      QuantityUnit quantityUnit = defaultQuantityUnit();
      Response response = getByQtyUnitCd( 200, Credentials.AUTHORIZED, QTY_UNIT_CD );
      assertQuantityUnit( quantityUnit, response );

   }


   @Test
   public void testGetCurrencyByCurrencyCodeNotFoundReturn404()
         throws JsonProcessingException, IOException {
      String invalidCurrencyCd = "ICC";
      getByQtyUnitCd( 404, Credentials.AUTHORIZED, invalidCurrencyCd );

   }


   @Test
   public void testGetByQtyUnitCdUnauthorizedReturns403() {
      getByQtyUnitCd( 403, Credentials.UNAUTHORIZED, QTY_UNIT_CD );
   }


   private Response getByQtyUnitCd( int statusCode, Credentials security, String qtyUnitCd ) {
      return getByQtyUnitCd( statusCode, security, qtyUnitCd, APPLICATION_JSON, APPLICATION_JSON );
   }


   private Response getByQtyUnitCd( int statusCode, Credentials security, String qtyUnitCd,
         String aContentType, String aAcceptType ) {
      String lUserName = security.getUserName();
      String lPassword = security.getPassword();

      Response lResponse = RestAssured.given().auth().preemptive().basic( lUserName, lPassword )
            .accept( aAcceptType ).contentType( aContentType ).expect().statusCode( statusCode )
            .when().get( QTY_UNIT_PATH + "/" + qtyUnitCd );
      return lResponse;
   }


   private void assertQuantityUnit( QuantityUnit expectedQtyUnit, Response actualQtyUnitResponse )
         throws JsonProcessingException, IOException {

      QuantityUnit actualQtyUnit = objectMapper
            .readValue( actualQtyUnitResponse.getBody().asString(), QuantityUnit.class );

      Assert.assertEquals( expectedQtyUnit, actualQtyUnit );

   }


   private QuantityUnit defaultQuantityUnit() {
      QuantityUnit quantityUnit = new QuantityUnit();
      quantityUnit.setCode( QTY_UNIT_CD );
      quantityUnit.setName( QTY_UNIT_SDESC );
      quantityUnit.setDecimalPlacesQuantity( DECIMAL_PLACES_QTY );
      return quantityUnit;

   }
}
