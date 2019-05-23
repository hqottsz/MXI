package com.mxi.am.api.resource.sys.refterm.measurementparameter;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mxi.am.api.annotation.CSIContractTest;
import com.mxi.am.api.annotation.CSIContractTest.Project;
import com.mxi.am.api.helper.Credentials;
import com.mxi.am.api.resource.ObjectMapperFactory;

import io.restassured.RestAssured;
import io.restassured.response.Response;


/**
 * e2e test for Measurement Parameter ResourceBean
 *
 */
public class MeasurementParameterResourceBeanTest {

   private static final String CODE1 = "TSIG";
   private static final String NAME1 = "Take Off Signal";
   private static final String DESCRIPTION1 = "TSIG description";
   private static final String UNIT_CODE1 = "DAY";
   private static final Integer PRECISION1 = 1;
   private static final String DOMAIN_TYPE_CODE1 = "CH";
   private static final String DATA_VALUE_CODE1 = "BUSY";
   private static final String DATA_VALUE_NAME1 = "BUSY Signal";
   private static final String DATA_VALUE_CODE2 = "UNCLEAR";
   private static final String DATA_VALUE_NAME2 = "UNCLEAR Signal";

   private static final String CODE2 = "CHAR";
   private static final String NAME2 = "Character Measurement";
   private static final String DESCRIPTION2 = "Character Measurement Test";
   private static final String UNIT_CODE2 = "COUNTS";
   private static final Integer PRECISION2 = 2;
   private static final String DOMAIN_TYPE_CODE2 = "CME";
   private static final String DATA_VALUE_CODE3 = "TEST";
   private static final String DATA_VALUE_NAME3 = "Test";

   private static final int MEASUREMENT_PARAMETER_RECORD_COUNT = 5;
   private static final String APPLICATION_JSON = "application/json";
   private static final String MEASUREMENT_PARAMETER_API_PATH =
         "/amapi/" + MeasurementParameter.PATH;


   @BeforeClass
   public static void setUpClass() {
      try {
         RestAssured.reset();
         RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
         RestAssured.baseURI = "http://".concat( InetAddress.getLocalHost().getHostName() );
         RestAssured.port = 80;
      } catch ( UnknownHostException e ) {
         throw new RuntimeException( "Cannot find local host name." );
      }
   }


   @Test
   @CSIContractTest( { Project.AFKLM_IMECH } )
   public void testGetMeasurementParameterByCodeSuccess200()
         throws JsonParseException, JsonMappingException, IOException {
      Response response = getMeasurementParameterByCode( 200, Credentials.AUTHENTICATED, CODE1 );
      assertMeasurementParmeterForGet( defaultMeasurementParameterBuilder(), response );
   }


   @Test
   @CSIContractTest( { Project.AFKLM_IMECH } )
   public void testSearchAllMeasurementParametersSuccess200()
         throws JsonParseException, JsonMappingException, IOException {
      Response response = searchAllMeasurementParameters( 200, Credentials.AUTHENTICATED );
      assertMeasurementParameterForSearch( defaultMeasurementParameterListBuilder(), response );
   }


   @Test
   public void testGetMeasurementParameterByCodeUnauthorizedFailure403() {
      getMeasurementParameterByCode( 403, Credentials.UNAUTHORIZED, CODE1 );
   }


   @Test
   public void testSearchAllMeasurementParmetersUnauthorizedFailure403() {
      searchAllMeasurementParameters( 403, Credentials.UNAUTHORIZED );
   }


   private List<MeasurementParameter> defaultMeasurementParameterListBuilder() {
      List<MeasurementParameter> measurementParameterList = new ArrayList<MeasurementParameter>();
      MeasurementParameter measurementParameter2 = new MeasurementParameter();

      List<DataValue> dataValueList = new ArrayList<DataValue>();
      DataValue dataValue3 = new DataValue();
      dataValue3.setCode( DATA_VALUE_CODE3 );
      dataValue3.setName( DATA_VALUE_NAME3 );
      dataValueList.add( dataValue3 );

      measurementParameter2.setCode( CODE2 );
      measurementParameter2.setName( NAME2 );
      measurementParameter2.setDescription( DESCRIPTION2 );
      measurementParameter2.setUnitCode( UNIT_CODE2 );
      measurementParameter2.setPrecision( PRECISION2 );
      measurementParameter2.setDomainTypeCode( DOMAIN_TYPE_CODE2 );
      measurementParameter2.setDataValue( dataValueList );

      MeasurementParameter measurementParameter1 = defaultMeasurementParameterBuilder();
      measurementParameter1.setDataValue( getExpectedDataValueList() );

      measurementParameterList.add( measurementParameter1 );
      measurementParameterList.add( measurementParameter2 );
      return measurementParameterList;
   }


   private MeasurementParameter defaultMeasurementParameterBuilder() {
      MeasurementParameter measurementParameter = new MeasurementParameter();
      measurementParameter.setCode( CODE1 );
      measurementParameter.setName( NAME1 );
      measurementParameter.setDescription( DESCRIPTION1 );
      measurementParameter.setUnitCode( UNIT_CODE1 );
      measurementParameter.setPrecision( PRECISION1 );
      measurementParameter.setDomainTypeCode( DOMAIN_TYPE_CODE1 );
      return measurementParameter;
   }


   private Response getMeasurementParameterByCode( int statusCode, Credentials security,
         String measurementParameterCode ) {
      String userName = security.getUserName();
      String password = security.getPassword();

      Response response = RestAssured.given().pathParam( "code", measurementParameterCode ).auth()
            .preemptive().basic( userName, password ).accept( APPLICATION_JSON )
            .contentType( APPLICATION_JSON ).expect().statusCode( statusCode ).when()
            .get( MEASUREMENT_PARAMETER_API_PATH + "/{code}" );
      return response;
   }


   private Response searchAllMeasurementParameters( int statusCode, Credentials security ) {
      String username = security.getUserName();
      String password = security.getPassword();

      Response response = RestAssured.given().auth().preemptive().basic( username, password )
            .accept( APPLICATION_JSON ).contentType( APPLICATION_JSON ).expect()
            .statusCode( statusCode ).when().get( MEASUREMENT_PARAMETER_API_PATH );
      return response;
   }


   private void assertMeasurementParmeterForGet( MeasurementParameter measurementParameterExpected,
         Response measurementParameterActual )
         throws JsonParseException, JsonMappingException, IOException {
      ObjectMapper objectMapper =
            ObjectMapperFactory.DEFAULT_OBJECT_MAPPER_FACTORY.createObjectMapper();
      MeasurementParameter actualMeasurementParameter =
            objectMapper.readValue( measurementParameterActual.getBody().asString(),
                  objectMapper.getTypeFactory().constructType( MeasurementParameter.class ) );
      List<DataValue> actualDataValueList = actualMeasurementParameter.getDataValue();
      Assert.assertTrue( "Incorrect data value list returned.",
            actualDataValueList.containsAll( getExpectedDataValueList() ) );
      actualMeasurementParameter.setDataValue( null );
      Assert.assertEquals( "Incorrect measurement parameter returned.",
            measurementParameterExpected, actualMeasurementParameter );
   }


   private void assertMeasurementParameterForSearch(
         List<MeasurementParameter> measurementParameterListExpected,
         Response measurementParameterListActual )
         throws JsonParseException, JsonMappingException, IOException {
      ObjectMapper objectMapper =
            ObjectMapperFactory.DEFAULT_OBJECT_MAPPER_FACTORY.createObjectMapper();
      List<MeasurementParameter> actualMeasurementParameterList = objectMapper.readValue(
            measurementParameterListActual.getBody().asString(), objectMapper.getTypeFactory()
                  .constructCollectionType( List.class, MeasurementParameter.class ) );
      assertEquals( "Expected 5 measurement parameters found in the database.",
            MEASUREMENT_PARAMETER_RECORD_COUNT, actualMeasurementParameterList.size() );
      Assert.assertTrue( "Incorrect measurement parameter list returned.",
            actualMeasurementParameterList.containsAll( measurementParameterListExpected ) );
   }


   private List<DataValue> getExpectedDataValueList() {
      List<DataValue> dataValueList = new ArrayList<DataValue>();
      DataValue dataValue1 = new DataValue();
      dataValue1.setCode( DATA_VALUE_CODE1 );
      dataValue1.setName( DATA_VALUE_NAME1 );
      DataValue dataValue2 = new DataValue();
      dataValue2.setCode( DATA_VALUE_CODE2 );
      dataValue2.setName( DATA_VALUE_NAME2 );
      dataValueList.add( dataValue1 );
      dataValueList.add( dataValue2 );
      return dataValueList;
   }
}
